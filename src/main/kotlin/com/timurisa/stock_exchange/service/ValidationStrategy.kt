package com.timurisa.stock_exchange.service

import com.timurisa.stock_exchange.domain.ClientAccount

interface ValidationStrategy {
    fun validate(clientAccount: ClientAccount)
}