package com.petcare.utils;

import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;
import com.petcare.domain.booking.Booking;

import java.util.*;

/**
 * Clase de utilidades estadísticas para analizar listas de citas {@link Booking},
 * permitiendo contar por estado o tipo, calcular porcentajes, y detectar el tipo más o menos demandado.
 * <p>
 * Todas sus funciones son estáticas y la clase no es instanciable.
 * </p>
 */

public final class BookingStats {

    /**
     * Constructor privado para evitar la instanciación.
     */
	
    private BookingStats() { }

    /**
     * Calcula la cantidad de citas agrupadas por estado.
     *
     * @param bookings Lista de objetos {@link Booking} (puede ser {@code null} o vacía).
     * @return Mapa con claves de tipo {@link BookingStatus} y su frecuencia correspondiente.
     *         Si la lista es {@code null}, se retorna un mapa vacío.
     */
    
    public static Map<BookingStatus, Integer> countByStatus(List<Booking> bookings) {
        Map<BookingStatus, Integer> result = new EnumMap<>(BookingStatus.class);
        if (bookings == null) return result;

        for (Booking booking : bookings) {
            BookingStatus status = booking.getStatus();
            result.put(status, result.getOrDefault(status, 0) + 1);
        }
        return result;
    }

    /**
     * Calcula la cantidad de citas agrupadas por tipo de servicio.
     *
     * @param bookings Lista de objetos {@link Booking} (puede ser {@code null} o vacía).
     * @return Mapa con claves de tipo {@link BookingType} y su frecuencia correspondiente.
     *         Si la lista es {@code null}, se retorna un mapa vacío.
     */
    
    public static Map<BookingType, Integer> countByType(List<Booking> bookings) {
        Map<BookingType, Integer> result = new EnumMap<>(BookingType.class);
        if (bookings == null) return result;

        for (Booking booking : bookings) {
            BookingType type = booking.getType();
            result.put(type, result.getOrDefault(type, 0) + 1);
        }
        return result;
    }

    /**
     * Calcula el porcentaje que representa cada tipo de cita sobre un total dado.
     * Los valores se redondean a 2 decimales.
     *
     * @param counts Mapa con conteos por tipo de cita.
     * @param total  Total de elementos (debe ser mayor que cero).
     * @return Mapa con claves de tipo {@link BookingType} y valores en porcentaje (0–100).
     *         Si el mapa es {@code null} o el total es 0, se retorna un mapa vacío.
     */
    
    public static Map<BookingType, Double> calculatePercentages(Map<BookingType, Integer> counts, int total) {
        Map<BookingType, Double> result = new EnumMap<>(BookingType.class);
        if (counts == null || total <= 0) return result;

        for (Map.Entry<BookingType, Integer> entry : counts.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / total;
            result.put(entry.getKey(), Math.round(percentage * 100.0) / 100.0);
        }
        return result;
    }

    /**
     * Obtiene el tipo de cita más solicitado en función del conteo.
     *
     * @param counts Mapa con conteos por tipo de cita.
     * @return {@link Optional} que contiene el tipo más demandado, o vacío si el mapa es {@code null} o está vacío.
     */
    
    public static Optional<BookingType> getMostDemanded(Map<BookingType, Integer> counts) {
        if (counts == null || counts.isEmpty()) return Optional.empty();

        BookingType maxType = null;
        int maxCount = Integer.MIN_VALUE;

        for (Map.Entry<BookingType, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxType = entry.getKey();
            }
        }
        return Optional.ofNullable(maxType);
    }

    /**
     * Obtiene el tipo de cita menos solicitado en función del conteo.
     *
     * @param counts Mapa con conteos por tipo de cita.
     * @return {@link Optional} que contiene el tipo menos demandado, o vacío si el mapa es {@code null} o está vacío.
     */
    
    public static Optional<BookingType> getLeastDemanded(Map<BookingType, Integer> counts) {
        if (counts == null || counts.isEmpty()) return Optional.empty();

        BookingType minType = null;
        int minCount = Integer.MAX_VALUE;

        for (Map.Entry<BookingType, Integer> entry : counts.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                minType = entry.getKey();
            }
        }
        return Optional.ofNullable(minType);
    }
}