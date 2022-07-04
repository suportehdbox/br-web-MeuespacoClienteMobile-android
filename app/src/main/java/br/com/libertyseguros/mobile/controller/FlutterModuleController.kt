package br.com.libertyseguros.mobile.controller

import android.content.Context
import br.com.libertyseguros.mobile.BuildConfig
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

// Configure an initial route.
// Start executing Dart code to pre-warm the FlutterEngine.
class FlutterModuleController(cntx: Context) {
    private val CHANNEL = "br.com.libertyseguros.flutter/channel"

    private val UPDATE_SCREEN_ROUTE = "/dialog-update"
    private var flutterEngine: FlutterEngine
    private var context: Context = cntx

    init {
        flutterEngine = FlutterEngine(context)
        flutterEngine.navigationChannel.setInitialRoute(UPDATE_SCREEN_ROUTE)
        flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        )
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getInfos") {
                val dict: HashMap<String, String> = HashMap<String, String>()
                if (BuildConfig.brandMarketing == 2) {
                    dict["brandMarketing"] = "Aliro"
                } else {
                    dict["brandMarketing"] = "Liberty"
                }
                dict["package"] = BuildConfig.APPLICATION_ID
                dict["debug"] = BuildConfig.DEBUG.toString()
                dict["prod"] = BuildConfig.prod.toString()

                result.success(dict)

            } else {
                result.notImplemented()
            }
        }
        FlutterEngineCache
                .getInstance()
                .put("default", flutterEngine)
    }

    fun ShowUpdateRequired() {
        context.startActivity(FlutterActivity
                .withCachedEngine("default")
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .build(context))
    }
}