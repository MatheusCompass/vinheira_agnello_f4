Setup rápido: JDK compatível e build do projeto

Por que isso é necessário
- O ambiente do Gradle/Kotlin usado por este projeto espera um JDK em uma faixa compatível (recomendado JDK 11 ou 17). JDKs muito novos (por exemplo Java 25) podem quebrar a avaliação do Kotlin DSL.
- Além disso, quando o projeto está em pastas do OneDrive ou caminhos com caracteres especiais, o Android Gradle Plugin pode alertar — incluí `android.overridePathCheck=true` em `gradle.properties` para mitigar essa verificação (use com cautela).

Passos recomendados (Windows, PowerShell)
1) Instale o JDK 17 (recomendado) se ainda não tiver:
   - Baixe do Adoptium: https://adoptium.net
   - Instale em, por exemplo, `C:\jdk-17`

2) Indique o JDK para o Gradle de forma local no projeto (recomendado):
   - Abra `gradle.properties` na raiz do projeto e adicione (ou descomente) a linha:
     org.gradle.java.home=C:/jdk-17
   - Salve o arquivo.

3) (Alternativa) Defina `JAVA_HOME` globalmente se preferir:
   - No PowerShell (executar como Administrador para persistir):
     ```powershell
     setx JAVA_HOME "C:\jdk-17" /M
     $env:JAVA_HOME = "C:\jdk-17"
     $env:Path = "C:\jdk-17\bin;" + $env:Path
     ```

4) Verifique a versão do Java no terminal (deve retornar 17.x):
   ```powershell
   java -version
   ```

5) Compile o app para forçar o processamento do Room (kapt) e verificar build:
   ```powershell
   .\gradlew.bat :app:assembleDebug
   ```

6) Se a build passar, rode o app em um emulador e abra o Logcat (tag "VinheriaDB" se ainda houver logs) ou use a UI Compose para adicionar/listar produtos.

Observações
- Mantive `android.overridePathCheck=true` em `gradle.properties` para reduzir problemas com OneDrive; remova se preferir.
- Se quiser, eu posso integrar Hilt para injeção de dependências e adicionar testes automáticos quando a build estiver funcionando.

