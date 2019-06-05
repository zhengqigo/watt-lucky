package org.fuelteam.watt.lucky.number;

public enum SizeUnit {
    BYTES, KILOBYTES, MEGABYTES, GIGABYTES;
    
    private final double BYTES_PER_KILOBYTE = 1024.0;
    private final double KILOBYTES_PER_MEGABYTE = 1024.0;
    private final double MEGABYTES_PER_GIGABYTE = 1024.0;

    public double toBytes(final long input) {
        double bytes;
        switch (this) {
            case BYTES:
                bytes = input;
                break;
            case KILOBYTES:
                bytes = input * BYTES_PER_KILOBYTE;
                break;
            case MEGABYTES:
                bytes = input * BYTES_PER_KILOBYTE * KILOBYTES_PER_MEGABYTE;
                break;
            case GIGABYTES:
                bytes = input * BYTES_PER_KILOBYTE * KILOBYTES_PER_MEGABYTE * MEGABYTES_PER_GIGABYTE;
                break;
            default:
                throw new RuntimeException("No value '" + this + "' recognized for enum MemoryUnit.");
        }
        return bytes;
    }

    public double toKiloBytes(final long input) {
        double kilobytes;
        switch (this) {
            case BYTES:
                kilobytes = input / BYTES_PER_KILOBYTE;
                break;
            case KILOBYTES:
                kilobytes = input;
                break;
            case MEGABYTES:
                kilobytes = input * KILOBYTES_PER_MEGABYTE;
                break;
            case GIGABYTES:
                kilobytes = input * KILOBYTES_PER_MEGABYTE * MEGABYTES_PER_GIGABYTE;
                break;
            default:
                throw new RuntimeException("No value '" + this + "' recognized for enum MemoryUnit.");
        }
        return kilobytes;
    }

    public double toMegaBytes(final long input) {
        double megabytes;
        switch (this) {
            case BYTES:
                megabytes = input / BYTES_PER_KILOBYTE / KILOBYTES_PER_MEGABYTE;
                break;
            case KILOBYTES:
                megabytes = input / KILOBYTES_PER_MEGABYTE;
                break;
            case MEGABYTES:
                megabytes = input;
                break;
            case GIGABYTES:
                megabytes = input * MEGABYTES_PER_GIGABYTE;
                break;
            default:
                throw new RuntimeException("No value '" + this + "' recognized for enum MemoryUnit.");
        }
        return megabytes;
    }

    public double toGigaBytes(final long input) {
        double gigabytes;
        switch (this) {
            case BYTES:
                gigabytes = input / BYTES_PER_KILOBYTE / KILOBYTES_PER_MEGABYTE / MEGABYTES_PER_GIGABYTE;
                break;
            case KILOBYTES:
                gigabytes = input / KILOBYTES_PER_MEGABYTE / MEGABYTES_PER_GIGABYTE;
                break;
            case MEGABYTES:
                gigabytes = input / MEGABYTES_PER_GIGABYTE;
                break;
            case GIGABYTES:
                gigabytes = input;
                break;
            default:
                throw new RuntimeException("No value '" + this + "' recognized for enum MemoryUnit.");
        }
        return gigabytes;
    }
}