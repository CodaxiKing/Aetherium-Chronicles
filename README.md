# Crônicas de Aetherium

Um mod abrangente para Minecraft 1.21.1 que adiciona três sistemas interconectados: criaturas épicas, tecnologia avançada e magia profunda.

## Visão Geral

**Crônicas de Aetherium** é um mod inspirado nos grandes mods clássicos como OreSpawn, Create, Applied Energistics, Botania e Ice and Fire. O mod oferece uma experiência completa de progressão através de três pilares principais:

### 🐉 Sistema de Criaturas e Exploração
- **20+ mobs únicos** com comportamentos avançados e IA personalizada
- **3 chefes épicos** com batalhas multi-fase e mecânicas únicas
- **Estruturas procedimentais massivas** espalhadas por todas as dimensões
- Sistema de spawn inteligente baseado em biomas

### ⚙️ Sistema de Tecnologia e Automação
- **Energia cinética visual** com engrenagens, correias e máquinas dinâmicas
- **Armazenamento digital** com autocrafting avançado estilo Applied Energistics
- **Processamento de minérios** avançado com proporções de 3:1 e 4:1
- **Geração de energia** através de moinhos, painéis solares e reatores

### ✨ Sistema de Magia e Misticismo
- **Magia baseada na natureza** usando plantas e cristais para gerar mana
- **Criaturas míticas** domesticáveis como dragões e guardiões antigos
- **Sistema de feitiços** totalmente customizável com componentes combináveis
- **Artefatos raros** não-craftáveis com habilidades únicas

## Instruções de Configuração do Ambiente no IntelliJ IDEA

Este guia irá orientá-lo na configuração do ambiente de desenvolvimento para o projeto **Crônicas de Aetherium**.

### 1. Pré-requisitos

**Java Development Kit (JDK):** Certifique-se de ter a versão correta do JDK instalada. Para o Minecraft 1.21.1, é necessário o **JDK 21**. Você pode baixá-lo no:
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [Adoptium (OpenJDK)](https://adoptium.net/)

**IntelliJ IDEA:** Baixe e instale a versão mais recente do IntelliJ IDEA Community ou Ultimate do [site da JetBrains](https://www.jetbrains.com/idea/).

**Git:** Garanta que o Git esteja instalado no seu sistema. Baixe em [git-scm.com](https://git-scm.com/).

### 2. Clonando o Projeto

1. Abra seu terminal ou Git Bash
2. Navegue até o diretório onde deseja salvar o projeto
3. Clone o repositório:
   ```bash
   git clone [url_do_repositorio]
   cd cronicas-aetherium
   ```

### 3. Importando para o IntelliJ

1. **Inicie o IntelliJ IDEA**
2. Na tela de boas-vindas, selecione **"Open"**
3. Navegue até o arquivo `build.gradle` do projeto e selecione-o
4. Uma janela irá aparecer. Escolha **"Open as Project"**
5. O IntelliJ começará a importar o projeto e baixar as dependências via Gradle. **Isso pode levar vários minutos** na primeira vez

### 4. Configurando o Workspace

1. **Após a importação inicial**, abra a janela de ferramentas do Gradle no lado direito do IntelliJ:
   - `View → Tool Windows → Gradle`

2. **Navegue até** `cronicas-aetherium → Tasks → neogradle runs`

3. **As configurações de execução** são geradas automaticamente pelo NeoForge:
   - `runClient` - Para executar o cliente do Minecraft com o mod
   - `runServer` - Para executar um servidor de desenvolvimento
   - `data` - Para gerar dados do mod (receitas, loot tables, etc.)

4. **Após a importação**, clique no botão de recarregar no painel do Gradle para garantir que tudo esteja atualizado

### 5. Executando e Testando o Mod

1. **No canto superior direito** da janela do IntelliJ, você verá um menu para as configurações de execução

2. **Selecione** `runClient` neste menu

3. **Clique no botão verde de "Play"** ao lado para iniciar o cliente do Minecraft com seu mod já carregado para testes

### 6. Configurações Adicionais (Opcional)

**Para melhor experiência de desenvolvimento:**

1. **Configure o JDK no projeto:**
   - `File → Project Structure → Project`
   - Certifique-se de que está usando JDK 21

2. **Configure a codificação:**
   - `File → Settings → Editor → File Encodings`
   - Defina tudo para UTF-8

3. **Configure o Gradle:**
   - `File → Settings → Build, Execution, Deployment → Build Tools → Gradle`
   - Use a versão Gradle do wrapper do projeto

### 7. Compilando o Arquivo .jar do Mod

**Para gerar o arquivo .jar distribuível do mod:**

1. **Abra o painel do Gradle**
2. **Navegue até** `cronicas-aetherium → Tasks → build`
3. **Execute a tarefa** `build` com um duplo clique
4. **O arquivo .jar compilado** estará localizado no diretório `build/libs/`
   - O arquivo sem `-sources` ou `-javadoc` no nome é o que deve ser distribuído

### 8. Estrutura do Projeto

```
cronicas-aetherium/
├── src/main/java/com/cronicasaetherium/mod/
│   ├── CronicasAetherium.java          # Classe principal do mod
│   ├── registry/                        # Registro de itens, blocos e entidades
│   ├── items/                          # Classes de itens customizados
│   ├── blocks/                         # Classes de blocos customizados
│   ├── entities/                       # Classes de entidades/mobs
│   └── systems/                        # Sistemas de tecnologia e magia
├── src/main/resources/
│   ├── assets/cronicasaetherium/       # Texturas, modelos, sons
│   ├── data/cronicasaetherium/         # Receitas, loot tables, estruturas
│   └── META-INF/                       # Configurações do mod
├── build.gradle                        # Configuração do build
├── gradle.properties                   # Propriedades do projeto
└── README.md                           # Este arquivo
```

## Diretrizes de Desenvolvimento

### Versão e Framework
- **Minecraft:** 1.21.1
- **Mod Loader:** NeoForge 21.1.57
- **Java:** JDK 21

### Comentários no Código
⚠️ **IMPORTANTE:** Este é um requisito crucial do projeto.

- **Todos os comentários devem ser em português**
- Explique o propósito de cada pacote, classe e método complexo
- Esclareça o que linhas ou blocos de código específicos devem fazer
- **Objetivo:** Outro desenvolvedor deve entender facilmente a estrutura e funcionalidade apenas lendo os comentários

### Integração entre Sistemas
Os três sistemas principais estão interconectados:
- **Materiais raros de chefes** → Necessários para máquinas de alto nível
- **Energia tecnológica** → Pode alimentar rituais mágicos
- **Essência mágica** → Melhora eficiência de máquinas

### Balanceamento
- **Progressão equilibrada** através dos três sistemas
- **Incentivo à exploração** com recompensas únicas por bioma
- **Desafio crescente** com mecânicas cada vez mais complexas

### Performance
- **Modelos otimizados** para evitar lag
- **Texturas eficientes** com resoluções adequadas
- **Código otimizado** para grandes quantidades de entidades

## Documentação Interna

O mod inclui um **Tomo das Crônicas** craftável que serve como guia completo dentro do jogo, explicando:
- Como começar com cada sistema
- Receitas e mecânicas avançadas
- Localização de estruturas e materiais raros
- Estratégias para derrotar chefes

## Contribuindo

Para contribuir com o projeto:

1. **Fork** o repositório
2. **Crie uma branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. **Siga as diretrizes** de comentários em português
4. **Teste thoroughamente** suas mudanças
5. **Submeta um Pull Request** com descrição detalhada

## Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo LICENSE para detalhes.

## Créditos e Inspirações

- **OreSpawn:** Sistema de criaturas e chefes épicos
- **Create:** Máquinas visuais e energia cinética
- **Applied Energistics 2:** Armazenamento digital e automação
- **Botania:** Magia baseada na natureza
- **Ice and Fire:** Criaturas míticas e artefatos
- **Mekanism:** Processamento avançado de recursos

---

*Desenvolvido com ❤️ para a comunidade brasileira de Minecraft*