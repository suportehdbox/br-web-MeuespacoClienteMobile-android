
Android APP
=================================

Requisitos
-------

  - Android Studio e SDK
  - Flutter SDK
  - Java
  
Setup
-------

  - Clone o repositório do projeto android e o flutter modules em uma mesma pasta, para que mantenham os caminhos relativos.
  - No projeto do flutter instale as dependecias pelo seguinte comando: flutter build apk
  - Ao execeutar o build no Android Studio, ele automaticamente instalará as dependencias necessárias para executar o projeto.

Compilação para ACT/PRD
-------

  - No arquivo app/build.gradle, dentro da estrutura de productFlavors altere a variavel "prod" para true para que a compilação em Produção e "false" para compilar em ACT
  - Nessa estrutura também temos os produtos Liberty e Aliro, entao ao compilar é possível escolher os apps que deseja compilar, além disso é ai que alteramos o app escolhido para compilar, no caso isDefault.set(true) trocando entre os flavors você define qual será compilado e executado em tempo de execução/depuração.

Publicação Loja:
-------

[Developer Android Publish](https://developer.android.com/studio/publish/upload-bundle?hl=pt-br)

Pontos imortantes:
-------

  - Para desenvolvimento local a dica é utilizar um smartphone Android, é necessário ativar o modo desenvolvedor, se for preciso utilize execute o projeto pelo botão   , para iniciar em modo debug.
  - Dentro da pasta  app/src/ temos as pastas main, liberty e aliro, a main é  uma pasta compartilhada entre as duas aplicações temos assets, codigos java e resources, como imagens, textos, layouts.
    - Na pasta liberty ou aliro, temos a mesma estrutura, porém que ao executar ou compilar o app por exemplo para Liberty, se existir a mesma imagem ou layout ele irá considerar o que temos nas pasta liberty, com isso é que fazemos a customização de cada app.
    - No caso de strings ele ira concatenar, o que tiver no main + a pasta especifica, e com isso poderá ter textos diferentes para cada plataforma
  - O projeto está estruturado em MVC, então temos as pasta de Model com regras e conexões, as Controllers fazendo passagem entre a view e a model, e as Views que são nesse caso as Activitys do Android e possuem a montagem das telas, como os listeners dos eventos de UI.