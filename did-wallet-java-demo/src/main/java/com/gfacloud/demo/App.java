package com.gfacloud.demo;

import java.util.ArrayList;

import com.gfacloud.did.FileSystemStore;
import com.gfacloud.did.Identity;
import com.gfacloud.did.Key;
import com.gfacloud.did.KeyType;
import com.gfacloud.did.RawIdentity;
import com.gfacloud.did.Wallet;

public class App 
{
    public static void main( String[] args )
    {
        Wallet w = new Wallet(new FileSystemStore("./wallet"));

        Identity identity = genIdentity();

        w.put("User1", identity);

        printWallet(w);
    }

    static Identity genIdentity() {
        String keyID = "keys-1";
        String privateKeyHex = "a889f4da49ff8dd6b03d4334723fe3e5ff55ae6a2483de1627bec873b0b73e1e86eabd6abce2f96553251de61def0265784688ff712ce583621a5b181ef21639";
        String publicKeyHex = "86eabd6abce2f96553251de61def0265784688ff712ce583621a5b181ef21639";
        Key key = new Key(keyID, KeyType.Ed25519, privateKeyHex, publicKeyHex);

        String did = "did:example:3dda540891d14a1baec2c7485c273c00";
        RawIdentity rawIdentity = new RawIdentity(did, key);
        return rawIdentity;
    }

    static void printWallet(Wallet w) {
        ArrayList<String> list = w.list();
        for (String label: list) {

            Identity identity = w.get(label);
            if (identity != null && identity instanceof RawIdentity) {
                RawIdentity rid = (RawIdentity)identity;
                System.out.println(rid.getId());
                System.out.println(rid.getType());

                Key k = rid.getKey();
                System.out.println("Key: ");
                System.out.println(k.getId());
                System.out.println(k.getType());
                System.out.println(k.getPrivateKeyHex());
                System.out.println(k.getPublicKeyHex());
            }
        }
    }
}
