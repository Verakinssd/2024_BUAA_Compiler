package backend.LLVMIR;

public enum RegType {
    I32, I32_PTR, I32_PTR_PTR,
    I8, I8_PTR, I8_PTR_PTR,
    I1, I1_PTR, I1_PTR_PTR;

    public static RegType getPtr(RegType regType) {
        switch (regType) {
            case I32:
                return I32_PTR;
            case I8:
                return I8_PTR;
            case I1:
                return I1_PTR;
            case I32_PTR:
                return I32_PTR_PTR;
            case I8_PTR:
                return I8_PTR_PTR;
            case I1_PTR:
                return I1_PTR_PTR;
            default:
                return null;
        }
    }

    public static RegType getReference(RegType regType) {
        switch (regType) {
            case I32_PTR:
                return I32;
            case I8_PTR:
                return I8;
            case I1_PTR:
                return I1;
            case I32_PTR_PTR:
                return I32_PTR;
            case I8_PTR_PTR:
                return I8_PTR;
            default:
                return I1_PTR;
        }
    }

    public String toString() {
        switch (this) {
            case I32:
                return "i32";
            case I32_PTR:
                return "i32*";
            case I32_PTR_PTR:
                return "i32**";
            case I8:
                return "i8";
            case I8_PTR:
                return "i8*";
            case I8_PTR_PTR:
                return "i8**";
            case I1:
                return "i1";
            case I1_PTR:
                return "i1*";
            case I1_PTR_PTR:
                return "i1**";
            default:
                return null;
        }
    }
}
