package it.cambi.qrgui.response.model;

import it.cambi.qrgui.services.util.IConstants;

public enum ErtaQrGuiRoles implements IConstants
{

    FEPQRA(R_FEPQRA), FEPQR1(R_FEPQR1), FEPQR2(R_FEPQR2);

    private String role;

    ErtaQrGuiRoles(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }

    public static ErtaQrGuiRoles fromString(String text)
    {
        for (ErtaQrGuiRoles erta : ErtaQrGuiRoles.values())
        {
            if (erta.getRole().equalsIgnoreCase(text))
            {
                return erta;
            }
        }
        return null;
    }
}
