# Vinheria Agnello — Módulo de Persistência (Room + Hilt)

Este repositório contém a implementação da persistência local do sistema de estoque da Vinheria Agnello (atividade do curso). Nesta fase implementamos a camada local com Room, injeção com Hilt e testes instrumentados para o DAO.

Objetivos cobertos

- Persistência local com Room
- Entidade `Produto` e DAO com operações CRUD
- `VinheriaDatabase` (Room)
- `ProdutoRepository` (injeção via Hilt)
- `ProdutoViewModel` com Hilt
- UI em Jetpack Compose: lista, adicionar, editar e excluir
- Testes instrumentados (androidTest) para o DAO usando Room in‑memory

Arquivos principais

- `app/src/main/java/com/example/vinheira_agnello_f4/data/Produto.kt` — entidade
- `app/src/main/java/com/example/vinheira_agnello_f4/data/ProdutoDao.kt` — DAO (CRUD)
- `app/src/main/java/com/example/vinheira_agnello_f4/data/VinheriaDatabase.kt` — Room DB
- `app/src/main/java/com/example/vinheira_agnello_f4/data/ProdutoRepository.kt` — repository (Hilt)
- `app/src/main/java/com/example/vinheira_agnello_f4/viewmodel/ProdutoViewModel.kt` — ViewModel (Hilt)
- `app/src/main/java/com/example/vinheira_agnello_f4/ui/ProductListScreen.kt` — Compose UI (CRUD + edição)
- `app/src/main/java/com/example/vinheira_agnello_f4/VinheriaApplication.kt` — `@HiltAndroidApp`
- `app/src/main/java/com/example/vinheira_agnello_f4/di/DataModule.kt` — Hilt module
- `app/src/androidTest/java/com/example/vinheira_agnello_f4/ProdutoDaoTest.kt` — testes instrumentados do DAO

Requisitos (ambiente)

- JDK 17 instalado (ex.: `C:\Program Files\Java\jdk-17`)
- Android SDK (com `platform-tools`) instalado
- Android Studio (recomendado) com AVD configurado
- Gradle Wrapper (já incluído: `gradlew.bat`)

OBS: Em Windows, prefira abrir um novo PowerShell depois de alterar variáveis de ambiente (JAVA_HOME / ANDROID_SDK_ROOT) para que entrem em vigor.

Passo-a-passo (PowerShell) — configuração rápida

1. Verificar Java e definir JAVA_HOME (se necessário):

```powershell
java -version
setx JAVA_HOME "C:\Program Files\Java\jdk-17" /M
# Feche e abra um novo PowerShell antes de continuar
```

2. Verificar SDK / adb

```powershell
where.exe adb
adb devices
```

Se `adb` não estiver no PATH, adicione temporariamente (substitua <usuário> pelo seu nome):

```powershell
$env:Path = "C:\Users\<usuário>\AppData\Local\Android\Sdk\platform-tools;$env:Path"
adb devices
```

3. Iniciar emulador (Android Studio → Tools → AVD Manager → Start) ou via linha de comando:

```powershell
& "$env:ANDROID_SDK_ROOT\emulator\emulator.exe" -list-avds
& "$env:ANDROID_SDK_ROOT\emulator\emulator.exe" -avd <Seu_AVD>
```

Build e execução

1. Build debug (na raiz do projeto):

```powershell
.\gradlew.bat :app:assembleDebug
```

2. Rodar instrumented tests (requer emulador conectado):

```powershell
# roda todos os instrumented tests
.\gradlew.bat :app:connectedAndroidTest --info

# ou somente a classe DAO (mais rápido)
.\gradlew.bat :app:connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.vinheira_agnello_f4.ProdutoDaoTest --info
```

3. Alternativa: instalar APKs e rodar instrumentação via adb (manual):

```powershell
adb -s <deviceId> install -r .\app\build\outputs\apk\debug\app-debug.apk
adb -s <deviceId> install -r .\app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk
adb -s <deviceId> shell am instrument -w -r com.example.vinheira_agnello_f4.test/androidx.test.runner.AndroidJUnitRunner
```

Onde encontrar os relatórios e resultados

- Relatório HTML: `app/build/reports/androidTests/connected/index.html`
- XML de resultados: `app/build/outputs/androidTest-results/connected/`
- Additional output: `app/build/outputs/connected_android_test_additional_output/`

Notas técnicas e decisões importantes

- Hilt: para integrar Hilt corretamente foi necessário garantir que o plugin fosse resolvido pelo Gradle. Adicionei entradas necessárias no projeto (plugin alias / fallback no top-level `build.gradle.kts`).

- JavaPoet: durante a integração houve um conflito transitivo de versões que causava `NoSuchMethodError` na agregação do Hilt. Aplicamos uma resolução forçada para `com.squareup:javapoet:1.13.0` (veja `build.gradle.kts` top-level). Se atualizar dependências, revise esta configuração.

- exportSchema: atualmente `@Database(..., exportSchema = false)`. Se quiser versionar o esquema para suportar migrations, mude para `exportSchema = true` e gere a pasta `schemas/`.

Checklist de entrega (concluído)

- [x] Room configurado e `Produto` implementado
- [x] DAO com CRUD implementado
- [x] Repository e ViewModel com Hilt
- [x] UI Compose com adicionar/editar/excluir
- [x] Testes instrumentados do DAO presentes e validados (4/4 pass)
- [x] README com instruções básicas (este arquivo)

Melhorias opcionais (não obrigatórias para entrega)

- Adicionar `exportSchema = true` e gerar schemas para migrações
- Adicionar testes unitários JVM para `ProdutoRepository`/`ProdutoViewModel`
- Adicionar testes Compose para fluxo de UI
- Implementar API C# (ASP.NET Core) para sincronização (se for requisito)
- Criar GitHub Actions para rodar `./gradlew check` / testes

Próximos passos

Se deseja que eu gere alguma das melhorias opcionais (README mais detalhado com imagens, testes unitários, CI ou API C#), responda com a letra correspondente:

- A: README mais detalhado (com telas/imagens e exemplos de comandos) — posso gerar uma versão ampliada
- B: Adicionar testes unitários JVM para Repository/ViewModel
- C: Implementar pequenas melhorias de UX (validação + snackbar)
- D: Criar API C# (ASP.NET Core) para sincronização CRUD
- E: Gerar pipeline CI (GitHub Actions) que roda `./gradlew check`

---

*Última atualização:* README gerado e validado no repositório. Boa sorte na entrega!
