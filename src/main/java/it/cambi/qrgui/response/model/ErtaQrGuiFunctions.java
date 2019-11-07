package it.cambi.qrgui.response.model;

import it.cambi.qrgui.services.util.IConstants;

public enum ErtaQrGuiFunctions implements IConstants
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
