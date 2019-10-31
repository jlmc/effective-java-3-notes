package io.costax.datetime.time;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ISO_8601_Format {

    static final DateTimeFormatter TIME_FORMATTER_ISO_8601_TIME = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Felizmente, ao contrário do que acontece com os timezones,
     * existe um padrão que define um formato específico e não ambíguo
     * para intercâmbio de informações relacionadas a data e hora.
     * Este formato é definido pela norma ISO 8601.
     * <p>
     * A data 4 de Maio de 2018 segundo a norma ISO 8601 deve ser representada: 2018-05-04
     * (ano com 4 dígitos, hífen, mês com 2 dígitos, hífen, dia com 2 dígitos).
     * <p>
     * É um formato que evita qualquer ambiguidade (como a que pode ocorrer com 04/05/2018, por exemplo),
     * pois existe uma ordem bem definida que não depende de fatores culturais.
     * <p>
     * Os campos estão nesta ordem, com esta quantidade de dígitos.
     * Quando o valor do campo tiver menos dígitos (dia e mês menor que 10, ou ano menor que 1000),
     * ele é preenchido com zeros à esquerda – no exemplo, o mês 5 foi escrito como 05
     * e o dia 4, como 04. Se o ano fosse 900, seria escrito como 0900 .
     */
    @Test
    void data_iso_format() {
        final LocalDate localDate = LocalDate.of(2018, Month.MAY, 4);

        final String format = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        assertEquals("2018-05-04", format);
    }

    /**
     * Devemos representar o horas como 17:32
     * As horas têm sempre valores de 00 (meia-noite) até  23 (11 da noite) – não há os famosos "AM" e "PM" dos americanos.
     * <p>
     * Os campos das horas, minutos e segundos sempre são escritos com dois dígitos,
     * e, se o valor for menor que 10, é completado com um  zero à esquerda.
     * <p>
     * Note que no exemplo anterior coloquei as horas e minutos ( 17:32 ),
     * pois os segundos podem ser omitidos caso o valor seja zero.
     * <p>
     * Também são permitidas as frações de segundo, sem  um limite de casas decimais.
     * <p>
     * Exemplo:  17:32:10.188499274
     * representa 17 horas (ou 5 da tarde), 32 minutos, 10 segundos e 188499274 nanossegundos.
     */
    @Test
    void time_iso_format() {
        assertEquals("17:32:10", LocalTime.of(17, 32, 10).format(DateTimeFormatter.ISO_LOCAL_TIME));
        assertEquals("17:32", LocalTime.of(17, 32, 10).format(TIME_FORMATTER_ISO_8601_TIME));
        assertEquals("17:32:10.999123999", LocalTime.of(17, 32, 10, 999_123_999).format(DateTimeFormatter.ISO_TIME));
        assertEquals("17:32:10.999123999", LocalTime.of(17, 32, 10, 999_123_999).format(DateTimeFormatter.ISO_LOCAL_TIME));
    }

    /**
     * Quando houver uma data e uma hora, elas devem ser separadas pela letra T, sempre maiúscula.
     * <p>
     * Por exemplo, 4 de maio de 2018 às 5 da tarde ficaria 2018-05-04T17:00
     * a data ( 2018-05-04 ) fica separada das horas ( 17:00 ) pela letra  T.
     * <p>
     * Este formato, com os separadores - e  : , é chamado de formato estendido.
     * <p>
     * Também é possível usar o formato básico, sem os separadores,
     * então esta data e hora seria escrita como 20180504T1700 – note que ainda assim é preciso ter o T
     * separando a data e a hora.
     * <p>
     * Particularmente acho um pouco confuso e difícil de ler, e prefiro o formato estendido.
     */
    @Test
    void dateTime_iso_format() {
        final LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2018, Month.MAY, 24),
                LocalTime.of(13, 45, 21));

        assertEquals("2018-05-24T13:45:21", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertEquals("2018-05-24T13:45:21", localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Test
    void offset_dateTime_iso_format() {

        final LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2018, Month.MAY, 24),
                LocalTime.of(13, 45, 21));

        assertEquals("2018-05-24T13:45:21+01:00", OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(1)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        assertEquals("13:45:21+01:00", OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(1)).format(DateTimeFormatter.ISO_OFFSET_TIME));
        assertEquals("2018-05-24T13:45:21Z", OffsetDateTime.of(localDateTime, ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    /**
     * Unix Epoch - An instant of time or a	date selected as a point of	reference.
     * <p>
     * Epoch é um ponto	específico na linha	do tempo (uma data e hora com valor	bem	definido),
     * É usado como	o ponto	de referência a	partir da qual todos os	outros valores se baseiam.
     * <p>
     * Em computação, existem várias datas diferentes usadas como ponto	de referência (vários epochs).
     * Cada	uma	é usada	em uma situação distinta.
     *
     * <a href="https://en.wikipedia.org/wiki/Epoch#Notable_epoch_dates_in_computing/">epoch dates in computing</a>
     *
     * <p>
     * Um destes é o <b>Unix Epoch</b>, que é definido como	'1970-01-01T00:00Z' (1 de janeiro de 1970, à meia-noite, em UTC).
     * Este instante específico é definido com o valor zero, e qualquer	outra data e hora é	definida como uma quantidade de	tempo decorrida	a partir do	Unix Epoch.
     *
     * Em sistemas Unix esta quantidade era medida em segundos, mas hoje muitos sistemas usam milissegundos (milesimo de segundos)
     */
    @Test
    void unixEpoch() {
        final Instant unixEpoch = Instant.EPOCH;
        assertEquals(0L, unixEpoch.toEpochMilli());
        assertEquals("1970-01-01T00:00:00Z", DateTimeFormatter.ISO_INSTANT.format(unixEpoch));

        // Não é possivel converter uma data directamente para um instante (timestamp),
        // para conseguir definir um instante precisamos saber a 'data, hora e timezone/offset'
        final Instant instant = LocalDate.now().atTime(1, 50).toInstant(ZoneOffset.ofHours(1));
    }

    @Test
    void dateTimeFormatting() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // date/time
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                // offset (hh:mm - "+00:00" when it's zero)
                .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                // offset (hhmm - "+0000" when it's zero)
                .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
                // offset (hh - "Z" when it's zero)
                .optionalStart().appendOffset("+HH", "Z").optionalEnd()
                // create formatter
                .toFormatter();


        System.out.println(OffsetDateTime.parse("2022-03-17T23:13:05.000+0100", formatter));
        System.out.println(OffsetDateTime.parse("2022-03-17T23:13:05.000+01", formatter));
        System.out.println(OffsetDateTime.parse("2022-03-17T23:13:05.000+01:00", formatter));
        System.out.println(OffsetDateTime.parse("2022-03-17T23:13:05.000Z", formatter));
    }
}
