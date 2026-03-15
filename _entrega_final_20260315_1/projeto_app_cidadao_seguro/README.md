# Afirma+ - App Android de Vagas Afirmativas

O **Afirma+** e um aplicativo Android nativo em **Kotlin** para divulgar e facilitar o acesso a **vagas de emprego afirmativas**.
O foco e conectar talentos de grupos historicamente minorizados a oportunidades com criterios de inclusao, diversidade e equidade.

## Objetivo

- Centralizar vagas afirmativas em uma experiencia simples e acessivel no celular
- Apoiar candidaturas com informacoes claras sobre requisitos e beneficios
- Incentivar empregabilidade com foco em diversidade no mercado de trabalho

## Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- DataStore Preferences
- Coroutines + StateFlow
- OkHttp + Kotlin Serialization

## Arquitetura

Projeto organizado em camadas (estilo Clean Architecture):

- `domain`: regras de negocio, modelos e contratos
- `data`: fontes local/remota e repositorios
- `presentation`: telas, estados, viewmodels e navegacao
- `di`: provimento de dependencias

## Funcionalidades principais

- Cadastro e login de usuario com sessao persistida
- Listagem de vagas afirmativas com busca e filtros (cidade e tipo de contrato)
- Detalhes da vaga com informacoes para candidatura
- Favoritar vagas para acompanhar depois
- Tela "Minhas Oportunidades" com favoritas e candidaturas
- Perfil com edicao de dados e progresso de preenchimento
- Tela Sobre com proposta do aplicativo

## Como executar

1. Abra o projeto no Android Studio.
2. Utilize JDK 17.
3. Configure o `local.properties` com o caminho do Android SDK (se necessario).
4. Sincronize o Gradle.
5. Execute em emulador ou dispositivo Android.
