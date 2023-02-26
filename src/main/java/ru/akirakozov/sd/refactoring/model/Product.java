package ru.akirakozov.sd.refactoring.model;

public record Product(String name, long price) {
    public String valuesString() {
        return String.format(
                "%s\t%d",
                name,
                price
        );
    }
}
