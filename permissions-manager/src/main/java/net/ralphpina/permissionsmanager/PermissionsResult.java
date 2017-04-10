package net.ralphpina.permissionsmanager;

public final class PermissionsResult {
    private final boolean isGranted;
    private final boolean hasAskedForPermissions;

    PermissionsResult(boolean isGranted, boolean hasAskedForPermissions) {
        this.isGranted = isGranted;
        this.hasAskedForPermissions = hasAskedForPermissions;
    }

    public boolean hasAskedForPermissions() {
        return hasAskedForPermissions;
    }

    public boolean isGranted() {
        return isGranted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PermissionsResult that = (PermissionsResult) o;

        return isGranted == that.isGranted && hasAskedForPermissions == that.hasAskedForPermissions;

    }

    @Override
    public int hashCode() {
        int result = (isGranted ? 1 : 0);
        result = 31 * result + (hasAskedForPermissions ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PermissionsResult{" +
                "isGranted=" + isGranted +
                ", hasAskedForPermissions=" + hasAskedForPermissions +
                '}';
    }
}
