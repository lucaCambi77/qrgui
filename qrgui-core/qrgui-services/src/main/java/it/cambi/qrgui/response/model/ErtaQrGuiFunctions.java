package it.cambi.qrgui.response.model;

import static it.cambi.qrgui.util.IConstants.F_QRCG00;
import static it.cambi.qrgui.util.IConstants.F_QRCG01;
import static it.cambi.qrgui.util.IConstants.F_QRCINS;
import static it.cambi.qrgui.util.IConstants.F_QRCMOD;
import static it.cambi.qrgui.util.IConstants.F_QRQE00;
import static it.cambi.qrgui.util.IConstants.F_QRQINS;
import static it.cambi.qrgui.util.IConstants.F_QRQMOD;
import static it.cambi.qrgui.util.IConstants.F_QRRE00;
import static it.cambi.qrgui.util.IConstants.F_QRRINS;
import static it.cambi.qrgui.util.IConstants.F_QRRMOD;

public enum ErtaQrGuiFunctions
{

    QRCG00(F_QRCG00), QRCG01(F_QRCG01), QRCINS(F_QRCINS), QRCMOD(
            F_QRCMOD), QRRE00(F_QRRE00), QRRINS(
                    F_QRRINS), QRRMOD(F_QRRMOD), QRQMOD(F_QRQMOD), QRQE00(F_QRQE00), QRQINS(F_QRQINS);

    private String appFunction;

    ErtaQrGuiFunctions(String appFunction)
    {
        this.appFunction = appFunction;
    }

    public String getAppFunction()
    {
        return appFunction;
    }

}
