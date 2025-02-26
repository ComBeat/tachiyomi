package eu.kanade.tachiyomi.ui.browse.migration.search

import android.os.Bundle
import android.view.View
import eu.kanade.domain.manga.model.Manga
import eu.kanade.tachiyomi.source.CatalogueSource
import eu.kanade.tachiyomi.ui.browse.source.browse.BrowseSourceController
import eu.kanade.tachiyomi.ui.browse.source.browse.SourceItem
import eu.kanade.tachiyomi.util.system.getSerializableCompat

class SourceSearchController(
    bundle: Bundle,
) : BrowseSourceController(bundle) {

    constructor(manga: Manga? = null, source: CatalogueSource, searchQuery: String? = null) : this(
        Bundle().apply {
            putLong(SOURCE_ID_KEY, source.id)
            putSerializable(MANGA_KEY, manga)
            if (searchQuery != null) {
                putString(SEARCH_QUERY_KEY, searchQuery)
            }
        },
    )
    private var oldManga: Manga? = args.getSerializableCompat(MANGA_KEY)
    private var newManga: Manga? = null

    override fun onItemClick(view: View, position: Int): Boolean {
        val item = adapter?.getItem(position) as? SourceItem ?: return false
        newManga = item.manga
        val searchController = router.backstack.findLast { it.controller.javaClass == SearchController::class.java }?.controller as SearchController?
        val dialog =
            SearchController.MigrationDialog(oldManga, newManga, this)
        dialog.targetController = searchController
        dialog.showDialog(router)
        return true
    }

    override fun onItemLongClick(position: Int) {
        view?.let { super.onItemClick(it, position) }
    }
}

private const val MANGA_KEY = "oldManga"
