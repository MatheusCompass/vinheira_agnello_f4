# Vinheria Agnello - Sistema de Estoque

Sistema de gerenciamento de estoque da Vinheria Agnello desenvolvido como projeto acadêmico, com frontend Android (Jetpack Compose + Room) e backend C# (Entity Framework Core + SQLite).

## Estrutura do Projeto

```
vinheira_agnello_f4/
├── app/                    # Frontend Android (Kotlin + Jetpack Compose)
├── backend-csharp/         # Backend C# (Console App + EF Core)
└── README.md              # Este arquivo
```

---

## Frontend Android

### Pré-requisitos

- **JDK 11 a 17** (recomendado: JDK 17)
  - Não use JDK 18+ (incompatível com Kotlin DSL)
- **Android SDK** com API 24+ (Android 7.0)
- **Emulador Android** ou dispositivo físico conectado

### Configuração do JDK

#### Windows (PowerShell como Administrador)
```powershell
# Definir JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M

# Verificar versão
java -version
```

#### macOS/Linux (Terminal)
```bash
# Adicionar ao ~/.bashrc, ~/.zshrc ou equivalente
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home  # macOS
# ou
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk  # Linux

# Recarregar configuração
source ~/.bashrc  # ou ~/.zshrc

# Verificar versão
java -version
```

**Alternativa:** Configure localmente no projeto criando/editando `gradle.properties`:
```properties
org.gradle.java.home=/caminho/para/jdk-17
```

### Build e Execução

#### Windows
```powershell
# Build debug
.\gradlew.bat :app:assembleDebug

# Executar testes instrumentados (requer emulador/device conectado)
.\gradlew.bat :app:connectedAndroidTest --info
```

#### macOS/Linux
```bash
# Build debug
./gradlew :app:assembleDebug

# Executar testes instrumentados (requer emulador/device conectado)
./gradlew :app:connectedAndroidTest --info
```

### Executar no Android Studio

1. Abra o projeto na raiz (`vinheira_agnello_f4`)
2. Aguarde a sincronização do Gradle
3. Configure um AVD (Tools → AVD Manager) ou conecte um dispositivo físico
4. Clique em **Run** (▶️)

### Funcionalidades

- Tela de login inicial
- Listagem de produtos (vinhos)
- Adicionar, editar e deletar produtos
- Persistência local com Room Database
- Injeção de dependências com Hilt
- Interface em Jetpack Compose com Material Design 3

---

## Backend C#

O módulo de persistência backend está documentado em:

📄 **[backend-csharp/README.md](backend-csharp/README.md)**

### Quick Start

```bash
# Navegar até o projeto
cd backend-csharp/EstoqueVinheria

# Executar (Windows, macOS, Linux)
dotnet run
```

Requer **.NET SDK 8.0 ou 10.0** instalado.

---

## Tecnologias Utilizadas

### Frontend Android
- Kotlin 2.0.21
- Jetpack Compose
- Room Database
- Hilt (Dependency Injection)
- Material Design 3
- Gradle 8.13

### Backend C#
- C# .NET 10.0 (compatível com 8.0)
- Entity Framework Core
- SQLite
- Console Application

---

## Notas Técnicas

- O frontend usa persistência local com Room (sem necessidade de servidor backend para funcionar)
- O backend C# é independente, demonstrando persistência com EF Core
- Ambos os módulos podem ser executados separadamente
- Primeira execução de cada módulo cria banco de dados local com dados de exemplo

---

## Suporte

Para problemas de build no Android:
1. Verifique a versão do JDK (`java -version`)
2. Confirme que o Android SDK está instalado
3. Execute `./gradlew clean` antes de rebuildar
4. Verifique `gradle.properties` para configuração de JDK local

Para problemas no backend C#:
1. Verifique a versão do .NET (`dotnet --version`)
2. Execute `dotnet restore` para restaurar pacotes
3. Consulte o README específico em `backend-csharp/`
