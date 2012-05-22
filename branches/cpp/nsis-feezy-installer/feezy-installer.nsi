# Auto-generated by EclipseNSIS Script Wizard
# 21-mag-2012 18.10.52

Name Feezy

RequestExecutionLevel admin

# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION 1.0
!define COMPANY Oneitalia
!define URL http://www.oneitalia.it

# MUI Symbol Definitions
!define MUI_ICON img\ico_48x48.ico
!define MUI_FINISHPAGE_NOAUTOCLOSE

# Included files
!include util\localization-loader.nsh
!include LogicLib.nsh
!include Sections.nsh
!include MUI2.nsh

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "img\header.bmp" ; optional
!define MUI_ABORTWARNING

!define MUI_WELCOMEFINISHPAGE_BITMAP "img\shoulder.bmp"
!define MUI_WELCOMEPAGE_TEXT $(WELCOME_TEXT)

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_FUNCTION launchFeezyAfterInstall
!define MUI_FINISHPAGE_RUN_TEXT $(LAUNCH_FEEZY_NOW)
!define MUI_FINISHPAGE_TEXT $(FINISH_PAGE_TEXT)

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE $(LicenseFile)
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

# Installer languages
!insertmacro LANG_LOAD "English"
!insertmacro LANG_LOAD "Italian"

!insertmacro MUI_RESERVEFILE_LANGDLL

LicenseLangString LicenseFile ${LANG_ENGLISH} "text\English\license.txt" 
LicenseLangString LicenseFile ${LANG_ITALIAN} "text\Italian\license.txt" 

# Installer attributes
OutFile installer\feezy-installer.exe
InstallDir $PROGRAMFILES\Feezy
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion 1.0.0.0
VIAddVersionKey ProductName Feezy
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey CompanyName "${COMPANY}"
VIAddVersionKey CompanyWebsite "${URL}"
VIAddVersionKey FileVersion "${VERSION}"
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright ""

!macro ABORT_WITH_MESSAGE MESSAGE
    DetailPrint ${MESSAGE}
    Abort
!macroend

Function launchFeezyAfterInstall
    Push $0   
    StrCpy $0 $DESKTOP\Feezy.lnk
    IfFileExists $0 0 linkAbsent
        ExecShell "open" $0 "" SW_SHOWDEFAULT
    linkAbsent:
    Pop $0
FunctionEnd

Function installFeezy    
    Push $0
    Push $1
    
    DetailPrint $(FEEZY_DOWNLOAD)
    
    StrCpy $0 $TEMP\FeezyApp.xap
    
    NSISdl::download \
        /TRANSLATE2 "$(DL_DOWNLOADING)" "$(DL_CONNECTING)" "$(DL_SECOND)" "$(DL_MINUTE)" "$(DL_HOUR)" \
        "$(DL_SECONDS)" "$(DL_MINUTES)" "$(DL_HOURS)" "$(DL_REMAINING)" \
        https://www.feezy.it/client/FeezyApp.xap $0
    
    Pop $1
    ${If} $1 == "success"
        Goto downloadOk
    ${ElseIf} $1 == "cancel"
        MessageBox MB_OK $(DOWNLOAD_CANCELLED)
        !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    ${Else}
        MessageBox MB_OK $(DOWNLOAD_ERROR)
        !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    ${EndIf}
        
    downloadOk:
    DetailPrint $(FEEZY_INSTALL)
    ExecWait '"$PROGRAMFILES\Microsoft Silverlight\sllauncher.exe" /install:$0 /origin:https://www.feezy.it/client/FeezyApp.xap /shortcut:desktop+startmenu'  
    
    Delete $0
    
    Pop $1
    Pop $0
FunctionEnd

Function installSilverlight    
    Push $0
    Push $1
    
    DetailPrint $(SL_DOWNLOAD)
    
    StrCpy $0 $TEMP\Silverlight.exe
    
    NSISdl::download \
        /TRANSLATE2 "$(DL_DOWNLOADING)" "$(DL_CONNECTING)" "$(DL_SECOND)" "$(DL_MINUTE)" "$(DL_HOUR)" \
        "$(DL_SECONDS)" "$(DL_MINUTES)" "$(DL_HOURS)" "$(DL_REMAINING)" \
        http://silverlight.dlservice.microsoft.com/download/E/4/4/E44E1840-4BBE-4CFE-AA06-E739131D6B7E/10411.00/runtime/Silverlight.exe $0
    
    Pop $1
    ${If} $1 == "success"
        Goto downloadOk
    ${ElseIf} $1 == "cancel"
        MessageBox MB_OK $(DOWNLOAD_CANCELLED)
        !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    ${Else}
        MessageBox MB_OK $(DOWNLOAD_ERROR)
        !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    ${EndIf}
    
    downloadOk:
    DetailPrint $(SL_INSTALL)
    ExecWait $0
        
    Delete $0
    
    Pop $1
    Pop $0    
FunctionEnd

# Installer sections
Section -Main SEC0000
    Push $0
    
    DetailPrint $(SL_VERSION_CHECK)
    
    ReadRegStr $0 HKLM Software\Microsoft\Silverlight "Version"

    ${If} $0 == ""
        MessageBox MB_OKCANCEL $(SL_INSTALL_MANDATORY) IDOK okToInstall IDCANCEL cancelInstall
    ${ElseIf} $0 < "5.0"
        DetailPrint "$(SL_VERSION) $0"
        MessageBox MB_OKCANCEL $(SL_UPDATE_MANDATORY) IDOK okToInstall IDCANCEL cancelInstall
    ${Else}
        Goto installFeezy
    ${Endif}
    
    cancelInstall:
    !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    
    okToInstall:
    DetailPrint "$(SL_VERSION) $0"
    Call installSilverlight 
    
    # Check if the user installed Microsoft Silverlight
    ReadRegStr $0 HKLM Software\Microsoft\Silverlight "Version"
    ${If} $0 < "5.0"
        !insertmacro ABORT_WITH_MESSAGE "$(INSTALLATION_ABORTED)"
    ${Endif}
    
    installFeezy:
    Call installFeezy
    
    Pop $0   
SectionEnd

# Installer functions
Function .onInit    
    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp img\splash.bmp
    advsplash::show 1500 200 400 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    
    #!insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

