# Estoque
Controle de estoque

# Pré requisitos:
- Baixar e instalar postgresql no modo administrador, deixar marcado os componentes "PostgreSQL Server" e "pgAdmin 4"
- Baixar e instalar JDK 8

# Configuração:
**Alternativa 1:**
- Abra o pgAdmin 4, defina sua senha para "123" e clique em lembrar senha
- Em Servers -> PostgreSQL 15 -> clique com o botão direito e crie um banco de dados chamado "EstoqueDB"
- Agora está tudo pronto para executar o "Estoque.jar"

**Alternativa 2:**
- Requisito: "postgresql-42.7.3.jar" (download: https://jdbc.postgresql.org/download/postgresql-42.7.3.jar)
- Abra o pgAdmin 4 e faça suas próprias configurações de servidor, conexão, banco de dados e senha
- Copie "Estoque" para a sua IDE e referencie a biblioteca "postgresql-42.7.3.jar"
![Exemplo](https://www.edureka.co/community/?qa=blob&qa_blobid=4867530673630722715)
- Faça as alterações de conexão no código em "Estoque/src/bd/ConexaoBD.java" nas linhas 10, 11 e 12
- Faça a exportação para um arquivo Runnable JAR file
- Execute o arquivo .jar criado
