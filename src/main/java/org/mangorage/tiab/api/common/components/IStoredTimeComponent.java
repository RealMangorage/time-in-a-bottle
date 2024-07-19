package org.mangorage.tiab.api.common.components;

/**
 * Defiines how much time a bottle has, and how much its gathered in total
 */
public interface IStoredTimeComponent {
    int stored();
    int total();
}
