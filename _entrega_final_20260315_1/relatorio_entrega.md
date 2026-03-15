# Relatorio do Aplicativo - Afirma+

## 1. Objetivo do aplicativo
O Afirma+ e um aplicativo Android para centralizar vagas de emprego afirmativas e facilitar o acesso de grupos historicamente minorizados a oportunidades com foco em inclusao, diversidade e equidade.

## 2. Tecnologia escolhida
- Linguagem: Kotlin
- Plataforma: Android nativo
- UI: Jetpack Compose (Material 3)
- Navegacao: Navigation Compose
- Persistencia local: DataStore Preferences
- Concorrencia e estado: Coroutines + StateFlow
- Consumo de API: OkHttp + Kotlin Serialization
- Build: Gradle (Kotlin DSL)

## 3. Aplicacao no contexto ESG
### Ambiental (E)
- Digitaliza e centraliza informacoes sobre vagas, reduzindo necessidade de materiais impressos no processo de busca.

### Social (S)
- Promove inclusao e empregabilidade por meio da divulgacao de vagas afirmativas.
- Facilita o acesso a oportunidades para pessoas de grupos sub-representados.

### Governanca (G)
- Estrutura clara de dados e fluxo de candidatura no app, favorecendo transparencia para o usuario.

## 4. Descricao e funcionalidades das telas (com imagens)
Observacao: inserir os prints das telas nos blocos abaixo.

### 4.1 Tela de Login
Funcionalidades:
- Autenticacao do usuario.
- Entrada para acesso ao fluxo principal do app.

Imagem:
![Tela de Login](./prints/tela-login.png)

Comentarios relevantes:
- [Adicionar observacoes sobre validacoes, fluxo e experiencia do usuario]

### 4.2 Tela Home
Funcionalidades:
- Exibe vagas afirmativas.
- Busca por termo.
- Filtros por cidade e tipo de contrato.
- Navegacao para detalhes da vaga.

Imagem:
![Tela Home](./prints/tela-home.png)

Comentarios relevantes:
- [Adicionar observacoes sobre ordenacao, filtros e usabilidade]

### 4.3 Tela Detalhes da Vaga
Funcionalidades:
- Exibe descricao completa da vaga.
- Mostra requisitos, beneficios e informacoes relevantes.
- Permite favoritar e/ou candidatar-se (quando aplicavel).

Imagem:
![Tela Detalhes da Vaga](./prints/tela-detalhes-vaga.png)

Comentarios relevantes:
- [Adicionar observacoes sobre clareza das informacoes e acoes disponiveis]

### 4.4 Tela Minhas Oportunidades
Funcionalidades:
- Lista vagas favoritadas.
- Lista candidaturas realizadas.
- Acesso rapido aos detalhes da vaga.

Imagem:
![Tela Minhas Oportunidades](./prints/tela-oportunidades.png)

Comentarios relevantes:
- [Adicionar observacoes sobre acompanhamento de progresso do usuario]

### 4.5 Tela Perfil
Funcionalidades:
- Visualizacao e edicao de dados do usuario.
- Indicacao de progresso de preenchimento de perfil.
- Acao de logout.

Imagem:
![Tela Perfil](./prints/tela-perfil.png)

Comentarios relevantes:
- [Adicionar observacoes sobre campos, completude e experiencia]

### 4.6 Tela Sobre
Funcionalidades:
- Apresenta o proposito do aplicativo.
- Resume proposta de valor e alinhamento com diversidade e inclusao.

Imagem:
![Tela Sobre](./prints/tela-sobre.png)

Comentarios relevantes:
- [Adicionar observacoes sobre contexto do projeto]

## 5. Endereco (https) do servico consumido
Servico principal consumido pelo aplicativo:
- https://apis.codante.io/api/job-board/jobs

## 6. Observacoes finais
- Build gerada em modo Release.
- Completar os prints e comentarios antes de exportar este markdown para PDF.
