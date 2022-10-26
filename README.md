# Calculadora de Créditos de Energia Solar
 Eu não sou programador, eu não sei nada, e tudo que sei aprendi com vídeos de indianos no Youtube. Boa sorte.

Criei esse app para Android para me ajudar a saber o quanto de créditos gerados pelo meu sistema fotovoltáico tenho na minha conta de luz durante o mês, assim eu fico sabendo se estou gerando créditos ou se meu consumo é muito grande e se estou consumindo os créditos da conta de luz. Moro numa cidade que é atendida pela Energisa S.A., sua região pode ser atendida por uma concessionária de energia elétrica difrente e as regras deles podem diferir das minhas.

# Como funciona a geração de energia solar?
A geração de energia fotovoltaica pode ter diferentes tipos produção, a mais comum aqui no Brasil é a geração on-grid na qual você está conectado a rede de energia elétrica convencional e tem um sistema fotovoltaico que gera energia para sua casa, todo o excedente (o que você não consumiu no momento) "sai" para a rede da rua e assim gera créditos em sua conta de energia. Durante a noite ou em dias em que não há sol, sua casa irá consumir esses créditos usando a energia da rua ao invés de pagar por essa energia consumida.

# Por que criar esse aplicativo?
Normamente quando se tem um sistema on-grid, a companhia de energia vai substituir seu relógio por um digital (se já não o é) que pode medir tanto a energia que entra como a energia que sai, chamado de medidor bidirecional. Todos os meses a companhia irá auferir seu relógio normalmente, porém além de registrar o consumo também irá registrar sua produção.
O problema é que eu gosto de saber quase todo dia se eu consumi muito ou, se foi um dia de muito sol, se eu produzi o bastante para manter meus créditos. Como a companhia mede somente uma vez por mês eu teria que esperar até o final do ciclo mensal, esperar receber a conta de energia para então descobrir se estou ou não com créditos. Com esse aplicativo eu só preciso ir até o relógio de energia e colocar os valores que estão lá para calcular exatamente o quanto consumi, o quanto produzi e quanto de crédito tenho guardado ou quanto de crédito eu "queimei".

# Como é feito o cálculo?
O cálculo (atualmente, pois num futuro esses créditos poderão ser reduzidos ou taxados) é bem simples: produção - consumo = créditos, ou seja, se você produziu 100 kw e consumiu 50 kw durante todo o mês, no fim do mês sua conta será de 0 kw (porque teoricamente não consumiu nada) e terá um crédito de 50 kw que poderão ser usados nos meses subsequentes. O problema é que nem sempre é o que acontece, as estações do ano influenciam a produção (sendo pior no inverno), pode ter uma semana de muita chuva, você pode decidir minerar bitcoin e consumir demais e poderá "queimar" seus créditos ou até pior, poderá gastar todos os seus créditos e então começar a pagar o excedente de consumo, como uma casa normal sem energia fotovoltaica, por isso é sempre bom ficar de olho todo dia ou toda semana como anda sua geração.

Além disso tudo, entra no cálculo o "valor mínimo", a companhia de energia, que não é tonta nem nada, interpreta que se há um consumo menor do que 50kw no mês automaticamente você paga 50kw (isso serve para qualquer casa, até mesmo as sem sistema fotovoltaico) independentemente se consumiu 10kw, 49kw ou se produziu 200kw e então consumiu 0kw. Portanto, imagine que nos últimos meses sua produção tenha sido ruim, você não tem mais créditos e no mês acabou consumindo mais do que gerou, vamos supor que consumiu 60kw a mais do que produziu, nesse caso como você já paga o valor mínimo de 50kw, vai ter que pagar mais 10kw de excedente, ao meu ver, podemos chamar esse valor mínimo de um buffer pois mesmo que tenha consumido a mais você só irá pagar a mais efetivamente se consumir mais de 50kw (atenção: isso é somente no caso de não haver créditos, pois se há você irá primeiro consumir seu créditos), agora imagine as mesmas condições e no mês consumiu 30kw a mais do que produziu, nesse caso como seu consumo foi menor que 50kw, você irá pagar 50kw e não irá adicionar nada a seus créditos, mas não irá pagar excedente, somente o valor mínimo.

Antes de falar como efetivamente o cálculo é feito, levando esses fatores em conta, é preciso dar nome aos burros. São quatro valores que temos que trabalhar: valor de produção inicial, valor de produção final, valor de consumo inicial e valor de consumo final. No meu relógio bidirecional esses valores de produção e consumo ficam aparecendo um após do outro de maneira intermitente, os valores têm um código que são mostrados no visor, o valor de consumo é código 03 e o valor de produção é o código 103. Na hora que o leiturista faz a leitura do mês é importantíssimo estar junto para pegar os valores de consumo e produção, que serão os valores iniciais (03_inicial e 103_inicial) pois a partir daí é que podemos fazer o cálculo nos dias subquentes pegando o valor que está mostrando no momento (final_03 e final_103)

Portanto o cálculo que efetivamente tem que ser feito é o seguinte:

## Variáveis: 
```
inicial_03, inicial_103, créditos, final_03, final_103
```

**inicial_03** significa o valor inicial do mês do consumo<br />
**inicial_103** significa o valor inicial da produção<br />
**Créditos significa** os valores dos créditos que ficaram do mês anterior<br />
**final_03 significa** o valor "final" do consumo<br />
**final_103 significa** o valor "final" da produção<br />


Desses valores podemos achar os seguintes resultados: **O consumo do relógio**, **a produção excedente que passou pelo relógio**, **a diferença entre eles** e **quanto de créditos temos**:
## Consumo
```Consumo = final_03 - inicial_03```
## Produção
```Produção = final_103 - inicial_103```
## Diferença
```Diferença = consumo - produção ```

## Créditos

- **Se a diferença for um número menor que -50** significa que a produção foi menor que o consumo e os créditos vão ser descontados, como a companhia cobra 50kw todos os meses, de qualquer jeito, só vai começar a descontar o que passar de 50<br />
ex.: se a diferença for -51kw só vai ser descontado dos créditos 1kw:
	
  ``` Creditos = creditos + diferença + 50```

- **Se a diferença for um número maior que -50** e menor de 0 significa que a produção foi menor que o consumo, mas todos os meses a companhia cobra 50kw de qualquer jeito então os créditos não serão descontados  
ex.: Se a diferença for 49kw não vai descontar dos créditos.  

	```Crédito fica igual ao final do mês passado```

- **Se for um número maior que 0** significa que a produção foi maior que o consumo e esse número vai ser somado aos créditos do mês passado  

	``` credito + diferença```

cansei de escrever, depois eu volto rs




- valor inicial do mês: inicial_código_03 = 7.270, inicial_código_103 = 8.238 

- Valor dos créditos do mês pasasdo = 1.283

- Passa uma semana e você vai no relógio ver quanto está mostrando:

- Valor atual(também chamado de final): final_código_03 = 7350 final_código_103 = 8315



