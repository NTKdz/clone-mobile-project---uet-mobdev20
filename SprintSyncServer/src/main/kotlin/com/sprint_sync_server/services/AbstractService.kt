package com.sprint_sync_server.services

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

abstract class AbstractService<T : Any>(
	private val repository: MongoRepository<T, String>
) {
	@Autowired
	protected lateinit var mongoTemplate: MongoTemplate

	fun existsById(id: String?) = id != null && repository.existsById(id)

	@JvmName("getAllByIds")
	fun getByIds(ids: List<String>): List<T> = repository.findAllById(ids)

	@JvmName("getAllByObjectIds")
	fun getByIds(ids: List<ObjectId>) = getByIds(ids.map { it.toString() })

	fun getById(id: String?) = id?.let { repository.findByIdOrNull(it) }

	fun getById(id: ObjectId) = getById(id.toString())

	fun save(entity: T) = repository.save(entity)

	@JvmName("deleteAllByIds")
	fun deleteByIds(ids: List<String>) = repository.deleteAllById(ids)

	@JvmName("deleteAllByObjectIds")
	fun deleteByIds(ids: List<ObjectId>) = deleteByIds(ids.map { it.toString() })

	fun deleteById(id: String) = repository.deleteById(id)

	fun deleteById(id: ObjectId) = deleteById(id.toString())

	open fun checkDates(vararg dates: String) = dates.forEach(LocalDateTime::parse)

	fun <T : Any> save(entity: T) = mongoTemplate.save(entity)

	fun <T, V> getList(key: String, value: V, clazz: Class<T>): List<T> {
		val query = Query(where(key).`is`(value))
		return mongoTemplate.find(query, clazz)
	}

	fun <T, V> getList(key: String, value: V?, valueList: List<V>?, clazz: Class<T>): List<T> {
		val criteria = where(key)
		val query = Query(if (value != null) criteria.`in`(value) else criteria.`in`(valueList!!))
		return mongoTemplate.find(query, clazz)
	}

	@JvmName("getByIdsObject")
	fun <T> getByIds(ids: List<ObjectId>, clazz: Class<T>) = getList("_id", null, ids, clazz)

	@JvmName("getByIdsString")
	fun <T> getByIds(ids: List<String>, clazz: Class<T>): List<T> = getByIds(ids.map { ObjectId(it) }, clazz)

	fun <T, V> getOne(key: String, value: V, clazz: Class<T>): T? {
		val query = Query(where(key).`is`(value))
		return mongoTemplate.findOne(query, clazz)
	}

	fun <T> getById(id: ObjectId, clazz: Class<T>) = getOne("_id", id, clazz)

	fun <T> getById(id: String, clazz: Class<T>) = getById(ObjectId(id), clazz)
}