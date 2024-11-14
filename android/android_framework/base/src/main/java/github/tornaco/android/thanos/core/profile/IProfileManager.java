/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/tornaco/Android/Sdk/build-tools/35.0.0/aidl -I/home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java -p/home/tornaco/Documents/Thanox/android/android_sdk/framework.aidl -p/home/tornaco/Documents/Thanox/android/android_sdk/thanos.aidl /home/tornaco/Documents/Thanox/android/android_framework/base/src/main/java/github/tornaco/android/thanos/core/profile/IProfileManager.aidl
 */
package github.tornaco.android.thanos.core.profile;
public interface IProfileManager extends android.os.IInterface
{
  /** Default implementation for IProfileManager. */
  public static class Default implements github.tornaco.android.thanos.core.profile.IProfileManager
  {
    @Override public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void addRule(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public void deleteRule(int ruleId) throws android.os.RemoteException
    {
    }
    @Override public boolean enableRule(int ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean disableRule(int ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isRuleEnabled(int ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean isRuleExists(int ruleId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setProfileEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isProfileEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setProfileEngineUiAutomationEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isProfileEngineUiAutomationEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setProfileEnginePushEnabled(boolean enabled) throws android.os.RemoteException
    {
    }
    @Override public boolean isProfileEnginePushEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setShellSuSupportInstalled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isShellSuSupportInstalled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean addConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean deleteConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.profile.ConfigTemplate> getAllConfigTemplates() throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.ConfigTemplate getConfigTemplateById(java.lang.String id) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAutoConfigTemplateSelection(java.lang.String id) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getAutoConfigTemplateSelectionId() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean applyConfigTemplateForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void addRuleIfNotExists(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public void publishStringFact(int source, java.lang.String factValue, long delayMills, java.lang.String[] args) throws android.os.RemoteException
    {
    }
    @Override public void updateRule(int ruleId, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
    {
    }
    @Override public void registerRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo getRuleById(int ruleId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean enableRuleByName(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean disableRuleByName(java.lang.String ruleName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
    {
    }
    @Override public android.os.ParcelFileDescriptor getLogFD() throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getLogPath() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void clearLogs() throws android.os.RemoteException
    {
    }
    @Override public void setLogEnabled(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isLogEnabled() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void executeAction(java.lang.String action) throws android.os.RemoteException
    {
    }
    @Override public void addConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException
    {
    }
    @Override public void removeConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo parseRuleOrNull(java.lang.String ruleString, int format) throws android.os.RemoteException
    {
      return null;
    }
    @Override public github.tornaco.android.thanos.core.profile.RuleInfo getRuleByName(java.lang.String ruleName) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setCustomSuCommand(java.lang.String command) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String getCustomSuCommand() throws android.os.RemoteException
    {
      return null;
    }
    // Engines.
    @Override public void addAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException
    {
    }
    @Override public void removeAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<github.tornaco.android.thanos.core.alarm.AlarmRecord> getAllAlarms() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAlarmEnabled(github.tornaco.android.thanos.core.alarm.Alarm alarm, boolean enabled) throws android.os.RemoteException
    {
    }
    // Danmu API
    @Override public void setDanmuUISettings(github.tornaco.android.thanos.core.profile.DanmuUISettings settings) throws android.os.RemoteException
    {
    }
    @Override public github.tornaco.android.thanos.core.profile.DanmuUISettings getDanmuUISettings() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements github.tornaco.android.thanos.core.profile.IProfileManager
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an github.tornaco.android.thanos.core.profile.IProfileManager interface,
     * generating a proxy if needed.
     */
    public static github.tornaco.android.thanos.core.profile.IProfileManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof github.tornaco.android.thanos.core.profile.IProfileManager))) {
        return ((github.tornaco.android.thanos.core.profile.IProfileManager)iin);
      }
      return new github.tornaco.android.thanos.core.profile.IProfileManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_setAutoApplyForNewInstalledAppsEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setAutoApplyForNewInstalledAppsEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isAutoApplyForNewInstalledAppsEnabled:
        {
          boolean _result = this.isAutoApplyForNewInstalledAppsEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.profile.IRuleAddCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.profile.IRuleAddCallback.Stub.asInterface(data.readStrongBinder());
          int _arg4;
          _arg4 = data.readInt();
          this.addRule(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteRule:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.deleteRule(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_enableRule:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.enableRule(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_disableRule:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.disableRule(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isRuleEnabled:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isRuleEnabled(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_isRuleExists:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.isRuleExists(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_checkRule:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.profile.IRuleCheckCallback _arg1;
          _arg1 = github.tornaco.android.thanos.core.profile.IRuleCheckCallback.Stub.asInterface(data.readStrongBinder());
          int _arg2;
          _arg2 = data.readInt();
          this.checkRule(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllRules:
        {
          github.tornaco.android.thanos.core.profile.RuleInfo[] _result = this.getAllRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getEnabledRules:
        {
          github.tornaco.android.thanos.core.profile.RuleInfo[] _result = this.getEnabledRules();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setProfileEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProfileEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isProfileEnabled:
        {
          boolean _result = this.isProfileEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addGlobalRuleVar:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _arg1;
          _arg1 = data.createStringArray();
          boolean _result = this.addGlobalRuleVar(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_appendGlobalRuleVar:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _arg1;
          _arg1 = data.createStringArray();
          boolean _result = this.appendGlobalRuleVar(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_removeGlobalRuleVar:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.removeGlobalRuleVar(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAllGlobalRuleVarNames:
        {
          java.lang.String[] _result = this.getAllGlobalRuleVarNames();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_getGlobalRuleVarByName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String[] _result = this.getGlobalRuleVarByName(_arg0);
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_getAllGlobalRuleVar:
        {
          github.tornaco.android.thanos.core.profile.GlobalVar[] _result = this.getAllGlobalRuleVar();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isGlobalRuleVarByNameExists:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isGlobalRuleVarByNameExists(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setProfileEngineUiAutomationEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProfileEngineUiAutomationEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isProfileEngineUiAutomationEnabled:
        {
          boolean _result = this.isProfileEngineUiAutomationEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setProfileEnginePushEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setProfileEnginePushEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isProfileEnginePushEnabled:
        {
          boolean _result = this.isProfileEnginePushEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setShellSuSupportInstalled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setShellSuSupportInstalled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isShellSuSupportInstalled:
        {
          boolean _result = this.isShellSuSupportInstalled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addConfigTemplate:
        {
          github.tornaco.android.thanos.core.profile.ConfigTemplate _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.profile.ConfigTemplate.CREATOR);
          boolean _result = this.addConfigTemplate(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_deleteConfigTemplate:
        {
          github.tornaco.android.thanos.core.profile.ConfigTemplate _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.profile.ConfigTemplate.CREATOR);
          boolean _result = this.deleteConfigTemplate(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAllConfigTemplates:
        {
          java.util.List<github.tornaco.android.thanos.core.profile.ConfigTemplate> _result = this.getAllConfigTemplates();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getConfigTemplateById:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.profile.ConfigTemplate _result = this.getConfigTemplateById(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setAutoConfigTemplateSelection:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.setAutoConfigTemplateSelection(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAutoConfigTemplateSelectionId:
        {
          java.lang.String _result = this.getAutoConfigTemplateSelectionId();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_applyConfigTemplateForPackage:
        {
          github.tornaco.android.thanos.core.pm.Pkg _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.pm.Pkg.CREATOR);
          github.tornaco.android.thanos.core.profile.ConfigTemplate _arg1;
          _arg1 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.profile.ConfigTemplate.CREATOR);
          boolean _result = this.applyConfigTemplateForPackage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addRuleIfNotExists:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          github.tornaco.android.thanos.core.profile.IRuleAddCallback _arg3;
          _arg3 = github.tornaco.android.thanos.core.profile.IRuleAddCallback.Stub.asInterface(data.readStrongBinder());
          int _arg4;
          _arg4 = data.readInt();
          this.addRuleIfNotExists(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_publishStringFact:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          long _arg2;
          _arg2 = data.readLong();
          java.lang.String[] _arg3;
          _arg3 = data.createStringArray();
          this.publishStringFact(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_updateRule:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          github.tornaco.android.thanos.core.profile.IRuleAddCallback _arg2;
          _arg2 = github.tornaco.android.thanos.core.profile.IRuleAddCallback.Stub.asInterface(data.readStrongBinder());
          int _arg3;
          _arg3 = data.readInt();
          this.updateRule(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_registerRuleChangeListener:
        {
          github.tornaco.android.thanos.core.profile.IRuleChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.profile.IRuleChangeListener.Stub.asInterface(data.readStrongBinder());
          this.registerRuleChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unRegisterRuleChangeListener:
        {
          github.tornaco.android.thanos.core.profile.IRuleChangeListener _arg0;
          _arg0 = github.tornaco.android.thanos.core.profile.IRuleChangeListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterRuleChangeListener(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getRuleById:
        {
          int _arg0;
          _arg0 = data.readInt();
          github.tornaco.android.thanos.core.profile.RuleInfo _result = this.getRuleById(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_enableRuleByName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.enableRuleByName(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_disableRuleByName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.disableRuleByName(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_dump:
        {
          github.tornaco.android.thanos.core.IPrinter _arg0;
          _arg0 = github.tornaco.android.thanos.core.IPrinter.Stub.asInterface(data.readStrongBinder());
          this.dump(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getLogFD:
        {
          android.os.ParcelFileDescriptor _result = this.getLogFD();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getLogPath:
        {
          java.lang.String _result = this.getLogPath();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_clearLogs:
        {
          this.clearLogs();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setLogEnabled:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setLogEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isLogEnabled:
        {
          boolean _result = this.isLogEnabled();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_executeAction:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.executeAction(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addConsoleLogSink:
        {
          github.tornaco.android.thanos.core.profile.ILogSink _arg0;
          _arg0 = github.tornaco.android.thanos.core.profile.ILogSink.Stub.asInterface(data.readStrongBinder());
          this.addConsoleLogSink(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeConsoleLogSink:
        {
          github.tornaco.android.thanos.core.profile.ILogSink _arg0;
          _arg0 = github.tornaco.android.thanos.core.profile.ILogSink.Stub.asInterface(data.readStrongBinder());
          this.removeConsoleLogSink(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_parseRuleOrNull:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          github.tornaco.android.thanos.core.profile.RuleInfo _result = this.parseRuleOrNull(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getRuleByName:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          github.tornaco.android.thanos.core.profile.RuleInfo _result = this.getRuleByName(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setCustomSuCommand:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.setCustomSuCommand(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getCustomSuCommand:
        {
          java.lang.String _result = this.getCustomSuCommand();
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_addAlarmEngine:
        {
          github.tornaco.android.thanos.core.alarm.Alarm _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.alarm.Alarm.CREATOR);
          this.addAlarmEngine(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeAlarmEngine:
        {
          github.tornaco.android.thanos.core.alarm.Alarm _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.alarm.Alarm.CREATOR);
          this.removeAlarmEngine(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAllAlarms:
        {
          java.util.List<github.tornaco.android.thanos.core.alarm.AlarmRecord> _result = this.getAllAlarms();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setAlarmEnabled:
        {
          github.tornaco.android.thanos.core.alarm.Alarm _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.alarm.Alarm.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setAlarmEnabled(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setDanmuUISettings:
        {
          github.tornaco.android.thanos.core.profile.DanmuUISettings _arg0;
          _arg0 = _Parcel.readTypedObject(data, github.tornaco.android.thanos.core.profile.DanmuUISettings.CREATOR);
          this.setDanmuUISettings(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getDanmuUISettings:
        {
          github.tornaco.android.thanos.core.profile.DanmuUISettings _result = this.getDanmuUISettings();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements github.tornaco.android.thanos.core.profile.IProfileManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAutoApplyForNewInstalledAppsEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isAutoApplyForNewInstalledAppsEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addRule(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(author);
          _data.writeInt(versionCode);
          _data.writeString(ruleJson);
          _data.writeStrongInterface(callback);
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteRule(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean enableRule(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableRule, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean disableRule(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_disableRule, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isRuleEnabled(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isRuleExists(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isRuleExists, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleJson);
          _data.writeStrongInterface(callback);
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllRules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getEnabledRules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setProfileEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProfileEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isProfileEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isProfileEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          _data.writeStringArray(varArray);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addGlobalRuleVar, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          _data.writeStringArray(varArray);
          boolean _status = mRemote.transact(Stub.TRANSACTION_appendGlobalRuleVar, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeGlobalRuleVar, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllGlobalRuleVarNames, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getGlobalRuleVarByName, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.GlobalVar[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllGlobalRuleVar, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(github.tornaco.android.thanos.core.profile.GlobalVar.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(varName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isGlobalRuleVarByNameExists, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setProfileEngineUiAutomationEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProfileEngineUiAutomationEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isProfileEngineUiAutomationEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isProfileEngineUiAutomationEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setProfileEnginePushEnabled(boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setProfileEnginePushEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isProfileEnginePushEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isProfileEnginePushEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setShellSuSupportInstalled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setShellSuSupportInstalled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isShellSuSupportInstalled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isShellSuSupportInstalled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean addConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, template, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addConfigTemplate, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean deleteConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, template, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteConfigTemplate, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.profile.ConfigTemplate> getAllConfigTemplates() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.profile.ConfigTemplate> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllConfigTemplates, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.profile.ConfigTemplate.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.profile.ConfigTemplate getConfigTemplateById(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.ConfigTemplate _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getConfigTemplateById, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.profile.ConfigTemplate.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAutoConfigTemplateSelection(java.lang.String id) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(id);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAutoConfigTemplateSelection, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getAutoConfigTemplateSelectionId() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAutoConfigTemplateSelectionId, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean applyConfigTemplateForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pkg, 0);
          _Parcel.writeTypedObject(_data, template, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_applyConfigTemplateForPackage, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addRuleIfNotExists(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(author);
          _data.writeInt(versionCode);
          _data.writeString(ruleJson);
          _data.writeStrongInterface(callback);
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addRuleIfNotExists, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void publishStringFact(int source, java.lang.String factValue, long delayMills, java.lang.String[] args) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(source);
          _data.writeString(factValue);
          _data.writeLong(delayMills);
          _data.writeStringArray(args);
          boolean _status = mRemote.transact(Stub.TRANSACTION_publishStringFact, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void updateRule(int ruleId, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          _data.writeString(ruleJson);
          _data.writeStrongInterface(callback);
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateRule, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void registerRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerRuleChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterRuleChangeListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo getRuleById(int ruleId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(ruleId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRuleById, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean enableRuleByName(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_enableRuleByName, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean disableRuleByName(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_disableRuleByName, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(p);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dump, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.os.ParcelFileDescriptor getLogFD() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.ParcelFileDescriptor _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLogFD, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.os.ParcelFileDescriptor.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String getLogPath() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLogPath, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void clearLogs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearLogs, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setLogEnabled(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLogEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isLogEnabled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isLogEnabled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void executeAction(java.lang.String action) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(action);
          boolean _status = mRemote.transact(Stub.TRANSACTION_executeAction, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(sink);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addConsoleLogSink, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(sink);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeConsoleLogSink, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo parseRuleOrNull(java.lang.String ruleString, int format) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleString);
          _data.writeInt(format);
          boolean _status = mRemote.transact(Stub.TRANSACTION_parseRuleOrNull, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public github.tornaco.android.thanos.core.profile.RuleInfo getRuleByName(java.lang.String ruleName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.RuleInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(ruleName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRuleByName, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.profile.RuleInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setCustomSuCommand(java.lang.String command) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(command);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCustomSuCommand, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String getCustomSuCommand() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCustomSuCommand, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      // Engines.
      @Override public void addAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, alarm, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAlarmEngine, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, alarm, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeAlarmEngine, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<github.tornaco.android.thanos.core.alarm.AlarmRecord> getAllAlarms() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<github.tornaco.android.thanos.core.alarm.AlarmRecord> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllAlarms, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(github.tornaco.android.thanos.core.alarm.AlarmRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAlarmEnabled(github.tornaco.android.thanos.core.alarm.Alarm alarm, boolean enabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, alarm, 0);
          _data.writeInt(((enabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAlarmEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      // Danmu API
      @Override public void setDanmuUISettings(github.tornaco.android.thanos.core.profile.DanmuUISettings settings) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, settings, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setDanmuUISettings, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public github.tornaco.android.thanos.core.profile.DanmuUISettings getDanmuUISettings() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        github.tornaco.android.thanos.core.profile.DanmuUISettings _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getDanmuUISettings, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, github.tornaco.android.thanos.core.profile.DanmuUISettings.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_setAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_isAutoApplyForNewInstalledAppsEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_addRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_deleteRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_enableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_disableRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_isRuleEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_isRuleExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_checkRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getAllRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getEnabledRules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_setProfileEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_isProfileEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_addGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_appendGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_removeGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_getAllGlobalRuleVarNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_getGlobalRuleVarByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_getAllGlobalRuleVar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_isGlobalRuleVarByNameExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_setProfileEngineUiAutomationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_isProfileEngineUiAutomationEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_setProfileEnginePushEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_isProfileEnginePushEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_setShellSuSupportInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_isShellSuSupportInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_addConfigTemplate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_deleteConfigTemplate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_getAllConfigTemplates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_getConfigTemplateById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_setAutoConfigTemplateSelection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_getAutoConfigTemplateSelectionId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_applyConfigTemplateForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_addRuleIfNotExists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
    static final int TRANSACTION_publishStringFact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
    static final int TRANSACTION_updateRule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
    static final int TRANSACTION_registerRuleChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
    static final int TRANSACTION_unRegisterRuleChangeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
    static final int TRANSACTION_getRuleById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
    static final int TRANSACTION_enableRuleByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
    static final int TRANSACTION_disableRuleByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
    static final int TRANSACTION_dump = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
    static final int TRANSACTION_getLogFD = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
    static final int TRANSACTION_getLogPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
    static final int TRANSACTION_clearLogs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
    static final int TRANSACTION_setLogEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
    static final int TRANSACTION_isLogEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
    static final int TRANSACTION_executeAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
    static final int TRANSACTION_addConsoleLogSink = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
    static final int TRANSACTION_removeConsoleLogSink = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
    static final int TRANSACTION_parseRuleOrNull = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
    static final int TRANSACTION_getRuleByName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
    static final int TRANSACTION_setCustomSuCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
    static final int TRANSACTION_getCustomSuCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
    static final int TRANSACTION_addAlarmEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
    static final int TRANSACTION_removeAlarmEngine = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
    static final int TRANSACTION_getAllAlarms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 56);
    static final int TRANSACTION_setAlarmEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 57);
    static final int TRANSACTION_setDanmuUISettings = (android.os.IBinder.FIRST_CALL_TRANSACTION + 58);
    static final int TRANSACTION_getDanmuUISettings = (android.os.IBinder.FIRST_CALL_TRANSACTION + 59);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "github.tornaco.android.thanos.core.profile.IProfileManager";
  public void setAutoApplyForNewInstalledAppsEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isAutoApplyForNewInstalledAppsEnabled() throws android.os.RemoteException;
  public void addRule(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException;
  public void deleteRule(int ruleId) throws android.os.RemoteException;
  public boolean enableRule(int ruleId) throws android.os.RemoteException;
  public boolean disableRule(int ruleId) throws android.os.RemoteException;
  public boolean isRuleEnabled(int ruleId) throws android.os.RemoteException;
  public boolean isRuleExists(int ruleId) throws android.os.RemoteException;
  public void checkRule(java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleCheckCallback callback, int format) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo[] getAllRules() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo[] getEnabledRules() throws android.os.RemoteException;
  public void setProfileEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isProfileEnabled() throws android.os.RemoteException;
  public boolean addGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException;
  public boolean appendGlobalRuleVar(java.lang.String varName, java.lang.String[] varArray) throws android.os.RemoteException;
  public boolean removeGlobalRuleVar(java.lang.String varName) throws android.os.RemoteException;
  public java.lang.String[] getAllGlobalRuleVarNames() throws android.os.RemoteException;
  public java.lang.String[] getGlobalRuleVarByName(java.lang.String varName) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.GlobalVar[] getAllGlobalRuleVar() throws android.os.RemoteException;
  public boolean isGlobalRuleVarByNameExists(java.lang.String varName) throws android.os.RemoteException;
  public void setProfileEngineUiAutomationEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isProfileEngineUiAutomationEnabled() throws android.os.RemoteException;
  public void setProfileEnginePushEnabled(boolean enabled) throws android.os.RemoteException;
  public boolean isProfileEnginePushEnabled() throws android.os.RemoteException;
  public void setShellSuSupportInstalled(boolean enable) throws android.os.RemoteException;
  public boolean isShellSuSupportInstalled() throws android.os.RemoteException;
  public boolean addConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException;
  public boolean deleteConfigTemplate(github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.profile.ConfigTemplate> getAllConfigTemplates() throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.ConfigTemplate getConfigTemplateById(java.lang.String id) throws android.os.RemoteException;
  public void setAutoConfigTemplateSelection(java.lang.String id) throws android.os.RemoteException;
  public java.lang.String getAutoConfigTemplateSelectionId() throws android.os.RemoteException;
  public boolean applyConfigTemplateForPackage(github.tornaco.android.thanos.core.pm.Pkg pkg, github.tornaco.android.thanos.core.profile.ConfigTemplate template) throws android.os.RemoteException;
  public void addRuleIfNotExists(java.lang.String author, int versionCode, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException;
  public void publishStringFact(int source, java.lang.String factValue, long delayMills, java.lang.String[] args) throws android.os.RemoteException;
  public void updateRule(int ruleId, java.lang.String ruleJson, github.tornaco.android.thanos.core.profile.IRuleAddCallback callback, int format) throws android.os.RemoteException;
  public void registerRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException;
  public void unRegisterRuleChangeListener(github.tornaco.android.thanos.core.profile.IRuleChangeListener listener) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo getRuleById(int ruleId) throws android.os.RemoteException;
  public boolean enableRuleByName(java.lang.String ruleName) throws android.os.RemoteException;
  public boolean disableRuleByName(java.lang.String ruleName) throws android.os.RemoteException;
  public void dump(github.tornaco.android.thanos.core.IPrinter p) throws android.os.RemoteException;
  public android.os.ParcelFileDescriptor getLogFD() throws android.os.RemoteException;
  public java.lang.String getLogPath() throws android.os.RemoteException;
  public void clearLogs() throws android.os.RemoteException;
  public void setLogEnabled(boolean enable) throws android.os.RemoteException;
  public boolean isLogEnabled() throws android.os.RemoteException;
  public void executeAction(java.lang.String action) throws android.os.RemoteException;
  public void addConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException;
  public void removeConsoleLogSink(github.tornaco.android.thanos.core.profile.ILogSink sink) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo parseRuleOrNull(java.lang.String ruleString, int format) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.RuleInfo getRuleByName(java.lang.String ruleName) throws android.os.RemoteException;
  public void setCustomSuCommand(java.lang.String command) throws android.os.RemoteException;
  public java.lang.String getCustomSuCommand() throws android.os.RemoteException;
  // Engines.
  public void addAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException;
  public void removeAlarmEngine(github.tornaco.android.thanos.core.alarm.Alarm alarm) throws android.os.RemoteException;
  public java.util.List<github.tornaco.android.thanos.core.alarm.AlarmRecord> getAllAlarms() throws android.os.RemoteException;
  public void setAlarmEnabled(github.tornaco.android.thanos.core.alarm.Alarm alarm, boolean enabled) throws android.os.RemoteException;
  // Danmu API
  public void setDanmuUISettings(github.tornaco.android.thanos.core.profile.DanmuUISettings settings) throws android.os.RemoteException;
  public github.tornaco.android.thanos.core.profile.DanmuUISettings getDanmuUISettings() throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
