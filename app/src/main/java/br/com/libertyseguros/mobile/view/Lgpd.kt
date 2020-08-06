package br.com.libertyseguros.mobile.view

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import br.com.libertyseguros.mobile.BuildConfig
import br.com.libertyseguros.mobile.R
import br.com.libertyseguros.mobile.model.BaseModel
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom


class Lgpd : BaseActionBar() {

    private lateinit var tvDados: TextView
    private lateinit var btAcessar: ButtonViewCustom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lgpd)
        mFirebaseAnalytics.setCurrentScreen(this, getString(R.string.title_action_bar_11), null)

        setTitle(getString(R.string.title_action_bar_15))

        val registerModel = BaseModel()
        tvDados = findViewById(R.id.tv_dados)
        btAcessar = findViewById(R.id.bt_acessar_canal)
        val builder = StringBuilder()
        builder.append(getString(R.string.lgpd_grupo_liberty))

        builder.append(" <u><a href='")
        if (BuildConfig.prod) {
            builder.append(getString(R.string.url_canal_report_prod))
        } else {
            builder.append(getString(R.string.url_canal_report_act))
        }
        builder.append("'>")
        builder.append(getString(R.string.lgpd_grupo_liberty_link))

        builder.append("</a></u>")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDados.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvDados.text = Html.fromHtml(builder.toString())
        }

        tvDados.setOnClickListener{ registerModel.openCanalReport(applicationContext) }
        btAcessar.setOnClickListener { registerModel.openCanalReport(applicationContext) }
    }
}
