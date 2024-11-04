<h1 align="center"> Wecli </h1>

## :pencil: Descrição do Projeto
Wecli é um aplicativo Android que fornece informações precisas e detalhadas sobre o clima atual e as previsões para os próximos dias. Com uma interface intuitiva e personalizável, o usuário pode facilmente obter as informações que precisa.

## :bar_chart: Status do Projeto
:construction: Projeto em desenvolvimento :construction:

## :dart: Funcionalidades e Demonstração da Aplicação

- `Funcionalidade 1`: Clima atual
- ![gif1](https://github.com/user-attachments/assets/545a00dd-7334-49df-9699-e43399a37f04)

- `Funcionalidade 2`: Previsões do clima para os próximos dias com intervalos de horas
- ![gif2](https://github.com/user-attachments/assets/4ca880fb-d311-41f8-b6e1-29ad20fd977c)

- `Funcionalidade 3`: Filtrar o dia desejado para obter a previsão
- ![gif3](https://github.com/user-attachments/assets/7d892494-f041-491f-9ab2-e3dc6df50f45)

- `Funcionalidade 4`: Detalhes da previsão do tempo
- ![gif-details-app](https://github.com/user-attachments/assets/048834fc-4325-4f7c-b92c-08a7022d15f1)

## 📁 Acesso ao projeto

Para ter acesso a versão mais estável do projeto, basta acessar: https://github.com/matheusfinamor1/wecli

## 🛠️ Abrir e rodar o projeto

- `Clonar o repositório:` Para clona-lo, basta acessar o link disponibilizado em "Acesso ao Projeto", clicar em "<> Code" e selecionar a opção desejada para realizar o clone do projeto (HTTP, SSH ou GitHub CLI).
- `Download`: Para fazer o download do projeto, basta acessar o link disponibilizado em "Acesso ao Projeto", clicar em "<> Code" e selecionar a opção "Download ZIP", onde o projeto será baixado em um arquivo .zip e para acessar seus arquivos, basta descompacta-lo na pasta que desejar de seu computador.

## 📌 Endpoints do Projeto

#### Retorna o clima atual

```http
  GET https://api.openweathermap.org/data/2.5/weather?lat=""&lon=""&appid=""&lang=""&units=""
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `appid` | `string` | **Obrigatório**. A chave da sua API obtida em [Weather API](https://openweathermap.org/api) |
| `lat` | `string` | **Obrigatório**. Latitude da localização do usuário |
| `lon` | `string` | **Obrigatório**. Longitude da localização do usuário |
| `lang` | `string` | Idioma dos dados fornecidos |
| `units` | `string` | Unidade de medida dos dados fornecidos |

#### Retorna a previsão do tempo

```http
  GET https://api.openweathermap.org/data/2.5/forecast?lat=""&lon=""&appid=""&lang=""&units=""&cnt=""
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `appid` | `string` | **Obrigatório**. A chave da sua API obtida em [Weather API](https://openweathermap.org/api) |
| `lat` | `string` | **Obrigatório**. Latitude da localização do usuário |
| `lon` | `string` | **Obrigatório**. Longitude da localização do usuário |
| `lang` | `string` | Idioma dos dados fornecidos |
| `units` | `string` | Unidade de medida dos dados fornecidos |
| `cnt` | `int` | Quantidade de datas/horas fornecidas |

## :computer: Tecnologias Utilizadas
- [Kotlin](https://kotlinlang.org/) - Linguagem de programação principal do projeto
- [Android Studio](https://developer.android.com/) - Ambiente de Desenvolvimento Integrado (IDE) utilizado para criar aplicativos Android
- [Compose](https://developer.android.com/compose) - Framework de UI moderno para criar interfaces de usuários nativas do Android
- [Glide](https://bumptech.github.io/glide/int/compose.html) - Biblioteca para carregar imagens
- [SplashScreen](https://developer.android.com/develop/ui/views/launch/splash-screen?hl=pt-br) - API para criar telas com carregamento personalizado
- [Material 3](https://m3.material.io/components) - Biblioteca de componentes visuais
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle?hl=pt-br) - Padrão de arquitetura para gerenciar ciclos de vida dos dados da UI
- [Koin](https://insert-koin.io/docs/quickstart/android/) - Framework de injeção de dependências (DI)
- [Ktor](https://ktor.io/) - Biblioteca HTTP para realizar requisições de rede
- [Compose - Navigation](https://developer.android.com/develop/ui/compose/navigation?hl=pt-br) - Biblioteca para navegação entre diferentes telas em aplicativos Compose
- [Play Services](https://developer.android.com/distribute/play-services?hl=pt-br) - Conjunto de serviços do Google que oferece funcionalidades como localização e mapas
- [Conscrypt](https://github.com/google/conscrypt) - Provedor de criptografia para garantir a segurança das comunicações

