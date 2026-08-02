package com.scatter97.chesscamerapgnmobile.domain.game

import fr.axl_lvy.stockfish_multiplatform.getStockfish

object StockfishBot {
    /** Uses bundled on-device Stockfish Lite; no network connection is needed while playing. */
    suspend fun chooseMove(fen: String, depth: Int = 8): String {
        val engine = getStockfish()
        engine.setOption("Threads", "1")
        engine.setPosition(fen = fen)
        return engine.search(depth = depth).bestMove
    }
}
