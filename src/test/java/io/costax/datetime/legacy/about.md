# Legacy Date time API

# java.util.Date

A classe Date não é uma data, porque ela não representa exatamente um único dia, mês e ano específicos. 

Na verdade, ela representa um ponto na linha do tempo (um Unix timestamp). 

como se sabe, o timestamp pode representar uma data e hora diferente, dependendo do timezone utilizado. 
Por exemplo, se criarmos um objeto Date contendo a data atual:

```java
Date now = new	Date();
```

Internamente, `new Date()` chama o method `System.currentTimeMillis()`, que por sua vez retorna  um `long` contendo o número de milissegundos desde o **Unix Epoch** (ou seja, o valor do timestamp).
Esse número é a única informação que o objecto Date possui. Qualquer outro valor (dia, mês, ano, hora, minuto, segundo) é calculado de acordo com o timezone padrão que é configurado na JVM. 



TODO: depending of System


`java.util.Date` possui metodos para obter campos especificos, como:

* `getMonth()` - obter o valor numérico do mês (-1)
* `getHours()` - obter horas

todos esses metodos usam o timezone padrão da JVM para calcular o valor do respectivo campo.
Porem todos esses getters estão deprecated desde a JDK 1.1 desde aí foi aconcelhada a utilização da classe `java.util.Calendar`
Alem disso alguns desses metodos não têm o nome mais adequado, por exemplo:

* getDate() - retorna o dia do mês [1-31]
* getDay() - retorna o dia da semana [0-Domingo, 6-Sabado]
* getTime() - retorna o valor do timestamp


## 8 formatacao


- Datas não têm formato. Elas só têm valores: 

- um `java.util.Date` possui o valor numérico do timestamp (quantidade de milissegundos decorridos deste o `Unix Epoch`:  1970-01-01T00:00Z ); 

- um `java.util.Calendar` possui um timestamp, um timezone e os respectivos campos: dia, mês, ano, hora, minuto, segundo etc.

    - Estes valores podem ser convertidos para um formato específico, 
    
- mas  Date  e  Calendar , por si só, não possuem nenhum formato. 

Quem possui um formato e é responsável por converter as datas de/para este formato é a classe `java.text.SimpleDateFormat`

