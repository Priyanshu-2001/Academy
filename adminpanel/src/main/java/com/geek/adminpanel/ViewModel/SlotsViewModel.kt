package com.geek.adminpanel.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geek.adminpanel.Repository.SlotRepository
import com.geek.adminpanel.dataModel.SlotData

class SlotsViewModel : ViewModel() {
    var slotData: MutableLiveData<ArrayList<SlotData>>? = null
    var data = MutableLiveData<ArrayList<SlotData>>()
    var repository = SlotRepository()
    private var totalCourt = MutableLiveData<String>()
    private var CourtCount: MutableLiveData<String>? = null

    fun getSlotsData(): LiveData<ArrayList<SlotData>> {
        if (slotData == null) {
            slotData = MutableLiveData<ArrayList<SlotData>>()
            data = repository.getSlots()
        }
        return data
    }

    fun deleteList(pos: Int) {
        repository.delete(pos)
        slotData = null
        getSlotsData()
    }

    fun getTotalCourt(): MutableLiveData<String> {
        if (CourtCount == null) {
            CourtCount = MutableLiveData()
            totalCourt = repository.getTotalCourt()
        }
        return totalCourt
    }

    fun saveCourtNumber(toString: String) {
        repository.updateTotalCourt(toString)
    }

    fun setNewSlot(id: String, timing: String) {
        repository.addNewSlot(id, timing)
        slotData = null
        getSlotsData()
    }

}