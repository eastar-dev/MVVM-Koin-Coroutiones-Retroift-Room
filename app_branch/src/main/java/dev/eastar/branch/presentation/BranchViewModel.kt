package dev.eastar.branch.presentation

import android.log.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import dev.eastar.branch.data.BranchEntity
import dev.eastar.branch.data.BranchRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class BranchViewModel(val repository: BranchRepository) : ViewModel() {

    val progress by lazy { MutableLiveData<Int>() }
    val branchsInMap by lazy { MutableLiveData<List<BranchEntity>>() }
    val branchsTeling by lazy { MutableLiveData<BranchEntity>() }

    fun getBranch() {
        viewModelScope.launch {
            try {
//            val data = repository.getBranchs()
                repository.getBranchs()
            } catch (e: Exception) {
                //TODO 공통 error 처리
            }
        }
    }

    fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
        searchInMap(mapView)
    }

    fun searchInMap(mapView: MapView) {
//        Log.e(mapView, mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.longitude
//                , mapView.mapPointBounds.topRight.mapPointGeoCoord.latitude
//                , mapView.mapPointBounds.topRight.mapPointGeoCoord.longitude
//                , mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.latitude)
        viewModelScope.launch {
            val branchsAll = repository.getBranchs()
            Log.e("queryed branchsAll.size", branchsAll.size)

            val branchs = repository.getBranchsByRect(mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.longitude
                    , mapView.mapPointBounds.topRight.mapPointGeoCoord.latitude
                    , mapView.mapPointBounds.topRight.mapPointGeoCoord.longitude
                    , mapView.mapPointBounds.bottomLeft.mapPointGeoCoord.latitude
            )
            branchsInMap.value = branchs
//            Log.e("queryed", branchsInMap.value)
            Log.e("queryed branchsAll.size", branchs.size)
        }
    }

    fun touchedPoiItem(branch: BranchEntity) {
        if (branch.tel?.replace("\\D".toRegex(), "").isNullOrBlank())
            return
        branchsTeling.value = branch
    }
}

val viewmodelModule = module {
    viewModel { BranchViewModel(get()) }
}