package com.example.levelupgamer.api

import com.example.levelupgamer.models.GamingProduct

class ProductRepository {
    private val api = RetrofitClient.api

    suspend fun getAllProducts(): Result<List<GamingProduct>> {
        return try {
            val response = api.getAllProducts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProduct(id: Int): Result<GamingProduct> {
        return try {
            val response = api.getProduct(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(product: GamingProduct): Result<GamingProduct> {
        return try {
            val response = api.createProduct(product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(id: Int, product: GamingProduct): Result<GamingProduct> {
        return try {
            val response = api.updateProduct(id, product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: Int): Result<Unit> {
        return try {
            val response = api.deleteProduct(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar producto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}