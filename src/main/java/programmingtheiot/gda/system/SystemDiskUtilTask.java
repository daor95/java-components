package programmingtheiot.gda.system;

import java.io.File;
import java.util.logging.Logger;

import programmingtheiot.common.ConfigConst;

/**
 * Shell representation of class for student implementation.
 *
 */
public class SystemDiskUtilTask extends BaseSystemUtilTask
{
    // private

    // constructors

    /**
     * Default.
     *
     */
    public SystemDiskUtilTask()
    {
        super(ConfigConst.NOT_SET, ConfigConst.DEFAULT_TYPE_ID);
    }


    // public methods

    @Override
    public float getTelemetryValue()
    {
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        double diskUtil = ((double) usedSpace / (double) totalSpace) * 100.0d;

        return (float) diskUtil;
    }
}