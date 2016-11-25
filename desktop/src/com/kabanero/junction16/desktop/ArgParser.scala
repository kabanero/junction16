package com.kabanero.junction16.desktop

import com.kabanero.junction16.GameConfig;

object ArgParser {
  def parse(args: Array[String]): GameConfig = {
    println(args.mkString("\n"))
    val config = GameConfig()
    config.host = args(0) == "true"
    config.address = args(1)
    config.port = args(2).toInt

    config
  }
}
