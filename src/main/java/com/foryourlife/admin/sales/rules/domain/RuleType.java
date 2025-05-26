package com.foryourlife.admin.sales.rules.domain;

public enum RuleType {
    DAY,           // Día específico de pago (Ejemplo: Domingo, Sábado)
    TIME_PERIOD,   // Jornada específica (Ejemplo: Mañana, Noche)
    BENEFIT,       // Para becas y participantes con beneficios
    EVENT,         // Eventos especiales como lanzamientos
    GIFT,          // Regalo, precio especial para obsequios
    WEEKEND,       // Precio especial en fines de semana
    MASTER,        // Precio especial para MASTER LIFE
    STANDARD,      // Precio normal sin descuentos
    TRAINING,      // Precio especial para pagos dentro de entrenamientos
    PRE_ENTRY      // Pago anticipado antes de ingresar a otra sesión

}
