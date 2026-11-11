package com.veltix.praktikum03

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MahasiswaAdapter(
    private val context: Context,
    private val data: MutableList<Mahasiswa>
) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = convertView
            ?: LayoutInflater.from(context)
                .inflate(R.layout.item_mahasiswa, parent, false)

        val mhs = data[position]
        view.findViewById<TextView>(R.id.tvNama).text = mhs.nama
        view.findViewById<TextView>(R.id.tvDetail).text =
            "${mhs.nim} \u2022 ${mhs.jurusan}"

        return view
    }
}