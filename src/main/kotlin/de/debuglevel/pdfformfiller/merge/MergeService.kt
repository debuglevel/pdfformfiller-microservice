package de.debuglevel.pdfformfiller.merge

import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class MergeService(
    private val mergeRepository: MergeRepository
) {
    private val logger = KotlinLogging.logger {}

    fun get(id: UUID): Merge {
        logger.debug { "Getting merge with ID '$id'..." }

        val merge: Merge = mergeRepository.findById(id).orElseThrow { MergeNotFoundException(id) }

        logger.debug { "Got merge with ID '$id': $merge" }
        return merge
    }

    fun add(merge: Merge): Merge {
        logger.debug { "Saving merge '$merge'..." }

        val addedMerge = mergeRepository.save(merge)

        logger.debug { "Saved merge: $addedMerge" }
        return merge
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting merge with ID '$id'..." }

        if (mergeRepository.existsById(id)) {
            mergeRepository.deleteById(id)
        } else {
            throw MergeNotFoundException(id)
        }

        logger.debug { "Deleted merge with ID '$id'" }
    }

    fun deleteAll() {
        logger.debug { "Deleting all merges..." }

        val countBefore = mergeRepository.count()
        mergeRepository.deleteAll()
        val countAfter = mergeRepository.count()

        logger.debug { "Deleted ${countBefore - countAfter} of $countBefore merges, $countAfter remaining" }
    }

    fun getList(): Set<Merge> {
        logger.debug { "Getting all merges..." }

        // TODO: improve performance by not retrieving the data field (and then not using it)
        val merges = mergeRepository.findAll().toSet()

        logger.debug { "Got all merges" }
        return merges
    }

    class MergeNotFoundException(id: UUID) : Exception("No merge found with ID '$id'")
}


