package dev.mina.currency.details

import dev.mina.currency.data.FixerAPI
import javax.inject.Inject

interface DetailsRepo

class DetailsRepoImpl @Inject constructor(private val dataSource: FixerAPI) : DetailsRepo