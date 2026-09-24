# 🗳️ Candidatos TSE: Eleições 2026 (MG)

Aplicação Spring Boot que lê o CSV oficial de candidatos do TSE (eleições gerais de 2026, Minas Gerais) e mostra as **1.832 candidaturas** em uma tela única, com foto, número, partido e filtro por cargo.

<img src="https://github.com/leonardoMartins-Dev/SpringBoot/blob/main/CandidatosTSE/CandidatosTSE/Captura%20de%20Tela%202026-09-24%20a%CC%80s%2019.25.06.png" width="1000"/>

A tela tem um topo escuro com o filtro de cargo e os botões **Filtrar** (verde) e **Limpar** (laranja), a contagem de candidatos encontrados e a grade de cards. O visual imita a urna eletrônica: cada card mostra o número do candidato em caixinhas, como na tela de votação, e os botões seguem as cores das teclas CONFIRMA e CORRIGE.

---

## 🚀 Como rodar

**Requisito:** Java 25. O Maven já vem no projeto (`mvnw`).

```bash
cd CandidatosTSE
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

Depois abra **http://localhost:8080**.

### Filtros pela URL

A tela filtra por cargo. O controller também aceita filtro por partido e busca por texto, mas esses dois só funcionam pela URL, porque ainda não têm campo na tela:

| Exemplo | O que mostra |
|---|---|
| `/?cargo=SENADOR` | Só os candidatos a senador |
| `/?partido=PT` | Só os candidatos do PT (sigla) |
| `/?texto=silva` | Candidatos com "silva" no nome, no nome de urna ou no número |
| `/?cargo=DEPUTADO FEDERAL&partido=PL` | Os filtros podem ser combinados |

---

## 📊 Os dados

- **Arquivo:** `consulta_cand_2026_MG.csv`, baixado do portal de Dados Abertos do TSE (separador `;`, aspas `"`, charset `ISO-8859-1`).
- **1.832 candidaturas** de **29 partidos**, distribuídas em 7 cargos:

| Cargo | Candidaturas |
|---|---:|
| Deputado Estadual | 998 |
| Deputado Federal | 756 |
| 1º Suplente | 19 |
| 2º Suplente | 19 |
| Senador | 18 |
| Governador | 11 |
| Vice-governador | 11 |

- **Fotos:** 1.831 fotos oficiais, no padrão do TSE `FMG<SQ_CANDIDATO>_div.jpg`. Só 3 candidaturas não têm foto própria. Uma delas pega a foto de outra candidatura da mesma pessoa (veja `resolverFotosPorCpf()`), e as outras 2 aparecem com o aviso "Sem foto".
- O CSV tem 50 colunas, mas a aplicação só lê 15 delas (UF, cargo, número, nomes, partido, CPF, nascimento, gênero, escolaridade, ocupação etc.). Os índices ficam em constantes `COL_*` no `CandidatosTseService`.

---

## 📁 Estrutura do projeto

```
📁 CandidatosTSE
│
├── 📁 src
│   └── 📁 main
│       │
│       ├── ☕ java
│       │   └── 📦 com.example.CandidatosTSE
│       │       │
│       │       ├── 🚀 application
│       │       │   └── CandidatosTseApplication.java
│       │       │       └── Classe principal da aplicação Spring Boot
│       │       │
│       │       ├── 🎮 controller
│       │       │   └── CandidatosTseController.java
│       │       │       └── Rota da tela única de candidatos
│       │       │
│       │       ├── 🧩 model
│       │       │   └── Candidato.java
│       │       │       └── Representa um candidato lido do CSV do TSE
│       │       │
│       │       └── ⚙️ service
│       │           └── CandidatosTseService.java
│       │               └── Carrega, trata e filtra os candidatos
│       │
│       └── 📁 resources
│           │
│           ├── 📊 data
│           │   └── 📁 candidatos
│           │       └── consulta_cand_2026_MG.csv
│           │           └── Base oficial do TSE (candidatos de MG, 2026)
│           │
│           ├── 🎨 static
│           │   │
│           │   ├── 🎨 css
│           │   │   └── style.css
│           │   │       └── Visual da tela (inspirado na urna eletrônica)
│           │   │
│           │   └── 🖼️ images
│           │       └── 📁 candidatos
│           │           └── Fotos oficiais dos candidatos (FMG<sq>_div.jpg)
│           │
│           └── 🌐 templates
│               └── index.html
│                   └── Tela única com filtro por cargo e grade de candidatos
│
└── 📄 pom.xml
    └── Dependências e configurações do Maven
```

---

## 📦 Dependências

Spring Boot **4.1.1** com Java **25**.

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.12.0</version>
</dependency>
```

- **opencsv:** leitura do CSV do TSE
- **spring-boot-starter-webmvc:** controller, rotas e servidor embutido (Tomcat)
- **spring-boot-starter-thymeleaf:** motor de templates da tela (`index.html`)

---

## ⚙️ `CandidatosTseService`

Carrega, trata e filtra os dados dos candidatos.

| Função | O que faz |
|---|---|
| `carregarCsv()` | Lê o CSV uma única vez, na inicialização (`@PostConstruct`). Ignora linhas com menos de 48 colunas, guarda a lista em memória e ordena por nome de urna. |
| `resolverFotosPorCpf()` | Quando uma candidatura não tem foto própria (ex.: titular de Senador), usa a foto de outra candidatura da mesma pessoa. Agrupa por **nome civil + número**, e não pelo CPF, porque o TSE mascara o CPF em algumas linhas. |
| `fotoExisteNoDisco()` | Verifica se o arquivo `FMG<sqCandidato>_div.jpg` existe no classpath. |
| `filtrar()` | Filtra por cargo, partido e/ou texto (nome, nome de urna ou número), sem diferenciar maiúsculas de minúsculas. Parâmetro vazio é ignorado. |
| `listarCargos()` / `listarPartidos()` | Retornam os valores distintos, em ordem alfabética, para montar os filtros. |

---

## 🎮 `CandidatosTseController`

| Endpoint | Método | Descrição |
|---|---|---|
| `/` | `GET` | Tela única da aplicação. Aceita os parâmetros opcionais `cargo`, `partido` e `texto`. Coloca no `Model` os candidatos filtrados (`candidatos`), o total (`totalEncontrado`), as listas de cargos e partidos (`cargo`, `partido`) e o cargo escolhido (`cargoSelecionado`, que mantém o filtro marcado na tela). Retorna a view `index`. |

---

## 🧩 `Candidato` (model)

Representa uma linha do CSV já traduzida para os campos usados na tela. É o objeto que vai do `Service` para o `Controller` e que o Thymeleaf usa direto no `index.html`. Além dos getters e setters, tem dois métodos calculados:

| Método | O que faz |
|---|---|
| `getNomeArquivoFoto()` | Monta o nome do arquivo da foto (`FMG<sq>_div.jpg`). Usa a foto emprestada quando `resolverFotosPorCpf()` definiu uma. |
| `getIdade()` | Calcula a idade a partir de `DT_NASCIMENTO` (`dd/MM/yyyy`). Retorna `-1` se a data vier vazia ou inválida, e aí a tela não mostra a idade. |

---

## 🎨 Tela (`index.html` + `style.css`)

Cada card mostra:

- cargo, número em caixinhas, nome de urna, nome civil e partido (passe o mouse na sigla para ver o nome completo);
- a foto oficial, ou o aviso "Sem foto" quando o arquivo não existe;
- idade, ocupação e escolaridade. O TSE manda esses textos em maiúsculas, e o CSS mostra só a primeira letra maiúscula ("Superior completo").

Detalhes:

- A **situação da candidatura** só aparece quando tem valor real. Neste CSV ela vem como `#NE` (não especificado) para todos, então fica escondida. Ocupação e escolaridade que começam com `#` também ficam escondidas.
- As fotos carregam só quando aparecem na tela (`loading="lazy"`). Isso faz diferença com mais de 1.800 imagens.
- O layout funciona no celular: o filtro ocupa a largura toda e a grade vira uma coluna.
- As fontes (Archivo e Chivo Mono) vêm do Google Fonts. Sem internet, a tela usa a fonte do sistema.
- O Thymeleaf guarda o template em cache. Se mudar o `index.html`, reinicie a aplicação. Mudanças no `style.css` aparecem só recarregando a página.

---

## 🔗 Documentação e links úteis

- [Dados Abertos do TSE: Candidatos 2026](https://dadosabertos.tse.jus.br/dataset/candidatos-2026)
- [opencsv no Maven Repository](https://mvnrepository.com/artifact/com.opencsv/opencsv)
- [Spring Boot: Spring MVC e templates](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html)
- [Thymeleaf: documentação](https://www.thymeleaf.org/documentation.html)
