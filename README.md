# Afirma+ (Kotlin Android)

Projeto convertido para Android nativo com Kotlin + Jetpack Compose.

## Stack

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- DataStore Preferences
- Coroutines + StateFlow
- OkHttp + Kotlin Serialization

## Arquitetura

Organizacao em camadas seguindo clean architecture:

- `domain`: modelos e contratos de repositorio
- `data`: fontes remota/local e implementacoes de repositorio
- `presentation`: telas, componentes, navegacao e viewmodels
- `di`: container simples para injecao de dependencias

## Funcionalidades

- Login e cadastro com sessao persistida
- Home com busca e filtros (cidade e tipo de contrato)
- Favoritar vaga
- Detalhes da vaga com candidatura
- Tela Sobre
- Tela Minhas Oportunidades (favoritas e candidaturas)
- Tela de Perfil com edicao e progresso de preenchimento
- Logout pelo topo e pelo perfil

## Como executar

1. Abra o projeto no Android Studio.
2. Use JDK 17.
3. Configure o `local.properties` com o caminho do Android SDK (se necessario).
4. Sincronize o Gradle.
5. Rode o app em emulador/dispositivo Android.

## Estrutura principal

- `app/src/main/java/com/anton/appcidadaoseguro`
- `app/src/main/assets/jobs.json`
- `app/build.gradle.kts`