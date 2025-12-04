# Frontend Android - Vinheria Agnello

Aplicativo Android de gerenciamento de estoque desenvolvido com Jetpack Compose, Room Database e Hilt.

## Tecnologias

- **Kotlin** 2.0.21
- **Jetpack Compose** - UI moderna e declarativa
- **Room Database** - Persistência local SQLite
- **Hilt** - Injeção de dependências
- **Material Design 3** - Sistema de design
- **Gradle** 8.13

## Pré-requisitos

- **JDK 11 a 17** (recomendado: JDK 17)
  - ⚠️ Não use JDK 18+ (incompatível com Kotlin DSL)
- **Android SDK** com API 24+ (Android 7.0)
- **Emulador Android** ou dispositivo físico com USB debugging

## Configuração do JDK

### Windows (PowerShell como Administrador)
```powershell
# Definir JAVA_HOME globalmente
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M

# Verificar versão
java -version
```

### macOS
```bash
# Adicionar ao ~/.zshrc ou ~/.bashrc
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home

# Recarregar configuração
source ~/.zshrc

# Verificar versão
java -version
```

### Linux
```bash
# Adicionar ao ~/.bashrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk

# Recarregar configuração
source ~/.bashrc

# Verificar versão
java -version
```

### Configuração Local (Alternativa)
Edite ou crie `gradle.properties` nesta pasta:
```properties
org.gradle.java.home=/caminho/para/jdk-17
```

## Build e Execução

### Windows
```powershell
# Build debug
.\gradlew.bat :app:assembleDebug

# Executar testes instrumentados (requer emulador/device)
.\gradlew.bat :app:connectedAndroidTest --info

# Limpar build
.\gradlew.bat clean
```

### macOS/Linux
```bash
# Build debug
./gradlew :app:assembleDebug

# Executar testes instrumentados (requer emulador/device)
./gradlew :app:connectedAndroidTest --info

# Limpar build
./gradlew clean
```

## Executar no Android Studio

1. Abra o **Android Studio**
2. File → Open → Selecione a pasta `frontend`
3. Aguarde a sincronização do Gradle
4. Configure um emulador (Tools → AVD Manager) ou conecte um dispositivo
5. Clique em **Run** (▶️)

## Estrutura do Projeto

```
frontend/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/vinheira_agnello_f4/
│   │   │   │   ├── VinheriaApplication.kt       # Application com Hilt
│   │   │   │   ├── LoginActivity.kt             # Tela de login
│   │   │   │   ├── MainActivity.kt               # Activity principal
│   │   │   │   ├── data/
│   │   │   │   │   ├── Produto.kt               # Entidade Room
│   │   │   │   │   ├── ProdutoDao.kt            # DAO (CRUD)
│   │   │   │   │   ├── VinheriaDatabase.kt      # Database Room
│   │   │   │   │   └── ProdutoRepository.kt     # Repository
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── ProdutoViewModel.kt      # ViewModel com Hilt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── ProductListScreen.kt     # Tela principal (Compose)
│   │   │   │   │   └── theme/                   # Tema Material 3
│   │   │   │   └── di/
│   │   │   │       └── DataModule.kt            # Módulo Hilt
│   │   │   └── res/                             # Recursos (imagens, strings)
│   │   └── androidTest/                         # Testes instrumentados
│   └── build.gradle.kts                         # Configuração do módulo app
├── build.gradle.kts                             # Configuração do projeto
├── settings.gradle.kts                          # Settings Gradle
└── gradle.properties                            # Propriedades Gradle
```

## Funcionalidades

### Tela de Login
- Validação de campos (usuário e senha)
- Navegação para tela principal após login

### Tela Principal (Estoque)
- ✅ Listar todos os produtos
- ➕ Adicionar novo produto
- ✏️ Editar produto existente
- 🗑️ Deletar produto
- 💾 Persistência automática com Room

### Campos do Produto
- **Nome** - Nome do vinho
- **Descrição** - Descrição detalhada
- **Uva** - Tipo de uva
- **Ano** - Ano de safra
- **Preço** - Valor unitário
- **Quantidade** - Estoque disponível

## Arquitetura

```
UI (Compose) → ViewModel → Repository → DAO → Room Database (SQLite)
       ↑                      ↑          ↑
       └────── Hilt DI ───────┴──────────┘
```

- **MVVM** - Model-View-ViewModel
- **Single Activity** - Navegação com Compose
- **Reactive UI** - StateFlow para atualizações automáticas
- **Dependency Injection** - Hilt para desacoplamento

## Primeira Execução

Na primeira execução, o app:
1. Cria o banco de dados `vinheria_database`
2. Insere 10 produtos de exemplo (vinhos)
3. Exibe a tela de login

## Testes

### Testes Instrumentados
Localização: `app/src/androidTest/`

```bash
# Executar todos os testes (requer emulador rodando)
./gradlew :app:connectedAndroidTest

# Executar teste específico do DAO
./gradlew :app:connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.example.vinheira_agnello_f4.ProdutoDaoTest
```

Relatórios: `app/build/reports/androidTests/connected/index.html`

## Resolução de Problemas

### Erro: "JAVA_HOME is not set"
Configure a variável de ambiente JAVA_HOME conforme instruções acima.

### Erro: "Unsupported class file major version"
Você está usando JDK 18+. Instale JDK 17 ou inferior.

### Erro: "SDK location not found"
Crie/edite `local.properties`:
```properties
sdk.dir=/caminho/para/Android/sdk
```

### Build muito lenta
Adicione ao `gradle.properties`:
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
```

### Emulador não aparece
1. Abra Android Studio → Tools → AVD Manager
2. Crie um novo AVD ou inicie um existente
3. Aguarde o emulador inicializar completamente

## Notas Técnicas

- **JavaPoet:** Versão forçada para 1.13.0 (resolve conflito do Hilt)
- **Export Schema:** Desabilitado (não há migrações versionadas)
- **OneDrive:** `android.overridePathCheck=true` ativo para compatibilidade

---

**Desenvolvido para a Fase 4 - Vinheria Agnello**
