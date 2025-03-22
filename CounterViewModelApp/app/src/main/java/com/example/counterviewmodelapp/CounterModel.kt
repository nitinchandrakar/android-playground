package com.example.counterviewmodelapp

data class CounterModel(var count : Int)

class CounterRepository{
    private var _counter = CounterModel(0)

    fun getCounter() = _counter

    fun incrementCounter(){
        _counter.count++
    }

    fun decrementCounter(){
        if(_counter.count>0){
            _counter.count--
        }
    }

}