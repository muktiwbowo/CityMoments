package com.svault.citymoments

import kotlinx.coroutines.flow.Flow

class MomentRepository(private val dao: MomentDao
) {
    fun getAllMoments(): Flow<List<ModelMoment>> {
        return dao.getAllMoments()
    }

    suspend fun addMoment(skill: ModelMoment) {
        dao.insertMoment(skill)
    }

    suspend fun deleteMoment(skill: ModelMoment) {
        dao.deleteMoment(skill)
    }
}