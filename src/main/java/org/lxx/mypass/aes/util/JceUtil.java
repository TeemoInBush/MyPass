package org.lxx.mypass.aes.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class JceUtil {

  /**
   * Remove Cryptography Restrictions
   * @author ntoskrnl
   *
   * Sometime app need uses 256-bit AES encryption,
   * usually we need install “Unlimited Strength” JCE policy files,
   * or skip the JCE API and use another cryptography library such as Bouncy Castle.
   *
   * ! Use another cryptography library's JCEProvider cant skip key strength restrictions !
   * So that code using reflection to change permissions without deploy any jar file.
   */
  public static void removeCryptographyRestrictions() {
    if (!isRestrictedCryptography()) {
      return;
    }
    try {
      /*
       * Do the following, but with reflection to bypass access checks:
       *
       * JceSecurity.isRestricted = false;
       * JceSecurity.defaultPolicy.perms.clear();
       * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
       */
      Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
      Constructor con = c.getDeclaredConstructor();
      con.setAccessible(true);
      Object allPermissionCollection = con.newInstance();
      Field f = c.getDeclaredField("all_allowed");
      f.setAccessible(true);
      f.setBoolean(allPermissionCollection, true);

      c = Class.forName("javax.crypto.CryptoPermissions");
      con = c.getDeclaredConstructor();
      con.setAccessible(true);
      Object allPermissions = con.newInstance();
      f = c.getDeclaredField("perms");
      f.setAccessible(true);
      ((Map) f.get(allPermissions)).put("*", allPermissionCollection);

      c = Class.forName("javax.crypto.JceSecurityManager");
      f = c.getDeclaredField("defaultPolicy");
      f.setAccessible(true);
      Field mf = Field.class.getDeclaredField("modifiers");
      mf.setAccessible(true);
      mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
      f.set(null, allPermissions);

    } catch (final Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Check JVM is Oracle JRE not OpenJDK
   */
  private static boolean isRestrictedCryptography() {
    // This simply matches the Oracle JRE, but not OpenJDK.
    return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
  }
}