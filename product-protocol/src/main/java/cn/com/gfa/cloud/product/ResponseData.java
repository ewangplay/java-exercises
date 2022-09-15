package cn.com.gfa.cloud.product;

import java.text.ParseException;
import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class ResponseData extends ASN1Object {

    private ASN1Integer version = new ASN1Integer(0);

    private ASN1Integer result;

    private ASN1GeneralizedTime respTime;

    public ResponseData(ASN1Integer version, ASN1Integer result, ASN1GeneralizedTime respTime) {
        this.version = version;
        this.result = result;
        this.respTime = respTime;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public ASN1Integer getresult() {
        return this.result;
    }

    public ASN1GeneralizedTime getReqTime() {
        return this.respTime;
    }

    private ResponseData(ASN1Sequence var1) {
        Enumeration var2 = var1.getObjects();
        if (var1.size() == 3) {
            this.version = ASN1Integer.getInstance(var2.nextElement());
            this.result = ASN1Integer.getInstance(var2.nextElement());
            this.respTime = ASN1GeneralizedTime.getInstance(var2.nextElement());
        }else {
            throw new IllegalArgumentException("sequence wrong size for a ResponseData");
        }
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(this.version);
        vector.add(this.result);
        vector.add(this.respTime);
        return new DERSequence(vector);
    }

    public static ResponseData getInstance(Object var0) {
        if (var0 instanceof ResponseData) {
            return (ResponseData)var0;
        } else {
            return var0 != null ? new ResponseData(ASN1Sequence.getInstance(var0)) : null;
        }
    }

    @Override
    public String toString() {
        try {
            return String.format("ResponseData: version=%d, result=%d, respTime=%s",
                    this.version.getValue().intValue(),
                    this.result.getValue().intValue(),
                    this.respTime.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
