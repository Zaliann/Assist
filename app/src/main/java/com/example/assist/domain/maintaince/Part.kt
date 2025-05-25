package com.example.assist.domain.maintaince

enum class Part(val partName: String, val mileageResource: Int) {
    Antifreeze("Антифриз",250_000),
    AirFilter("Воздушный фильтр",30_000),
    PowerSteeringFluid("Жидкость ГУР",50_000),
    TransmissionFluid("Масло в КПП",50_000),
    EngineOilFilter("Масляный фильтр",15_000),
    EngineOil("Моторное масло",15_000),
    DriveBelt("Приводной ремень",70_000), // приводной ремень
    TimingBelt("Ремень ГРМ",130_000), // ремень ГРМ
    SparkPlug("Свечи зажигания",30_000), // Свечи
    FuelFilter("Топливный фильтр",30_000),
    BrakeFluid("Тормозная жидкость",55_000),
    BrakeDiscs("Тормозные диски",100_000),
    BrakeShoe("Тормозные колодки",30_000), // тормозные колодки
    CabinAirFilter("Салонный фильтр",15_000)
}

