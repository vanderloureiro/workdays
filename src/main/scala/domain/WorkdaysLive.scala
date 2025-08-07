package dev.vanderloureiro
package domain

import zio.{ULayer, ZIO, ZLayer}

import java.time.LocalDate

case class WorkdaysLive() extends Workdays {

  
  override def isWorkday(date: String): ZIO[Workdays, Nothing, BooleanResponse] = {
    for {
      date <- parseDate(date)
      holidays <- calcHolidays(date.getYear)
      isHoliday <- ZIO.succeed(holidays.contains(date))
    } yield BooleanResponse(!isHoliday)
  }

  /*
  *Confraternização Universal	- 1º de janeiro	- Início do ano civil.
  Tiradentes	- 21 de abril	- Em homenagem ao mártir da Inconfidência Mineira Joaquim José da Silva Xavier, o Tiradentes.
  Dia do Trabalhador	- 1º de maio	- Homenagem a todos os trabalhadores.
  Dia da Pátria (Independência do Brasil) (Data Magna Nacional)	- 7 de setembro	- Proclamação da Independência em relação a Portugal.
  Nossa Senhora Aparecida	- 12 de outubro[5] - Padroeira do Brasil.
  Finados	- 2 de novembro	- Dia em memória aos mortos.
  Proclamação da República	- 15 de novembro	- Transformação do Império em República.
  Dia Nacional de Zumbi e da Consciência Negra	- 20 de novembro -	Data da Morte de Zumbi dos Palmares.[6]
  Natal	- 25 de dezembro	- Celebração do nascimento de Jesus.
  * */
  private def calcHolidays(year: Int): ZIO[Any, Nothing, List[LocalDate]] = {
    val diaPrimeiro = LocalDate.of(year, 1, 1)
    val tiradentes = LocalDate.of(year, 4, 21)
    val independencia = LocalDate.of(year, 9, 7)
    val nossaSenhora = LocalDate.of(year, 10, 12)
    val finados = LocalDate.of(year, 11, 2)
    val proclamacaoRepublica = LocalDate.of(year, 12, 15)
    val conscienciaNegra = LocalDate.of(year, 11, 20)
    val natal = LocalDate.of(year, 12, 25)

    val holidays = List(diaPrimeiro, tiradentes, independencia, nossaSenhora, finados, proclamacaoRepublica, conscienciaNegra, natal)
    ZIO.succeed(holidays)
  }

  private def parseDate(date: String): ZIO[Workdays, Nothing, LocalDate] = {
    for {
      parts <- ZIO.succeed(date.split("-"))
      finalDate <- ZIO.attempt{
        val day = parts(0).toInt
        val month = parts(1).toInt
        val year = parts(2).toInt
        LocalDate.of(year, month, day)
      }.catchAll(e => ZIO.log(s"error: ${e}").as(LocalDate.now()))
    } yield finalDate
  }
}

object WorkdaysLive {
  val live: ULayer[WorkdaysLive] = ZLayer.succeed(WorkdaysLive())
}
