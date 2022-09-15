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

public class RequestData extends ASN1Object {

    private ASN1Integer version = new ASN1Integer(0);

    private ASN1Integer operand;

    private ASN1GeneralizedTime reqTime;

    public RequestData(ASN1Integer version, ASN1Integer operand, ASN1GeneralizedTime reqTime) {
        this.version = version;
        this.operand = operand;
        this.reqTime = reqTime;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public ASN1Integer getOperand() {
        return this.operand;
    }

    public ASN1GeneralizedTime getReqTime() {
        return this.reqTime;
    }

    private RequestData(ASN1Sequence var1) {
        Enumeration var2 = var1.getObjects();
        if (var1.size() == 3) {
            this.version = ASN1Integer.getInstance(var2.nextElement());
            this.operand = ASN1Integer.getInstance(var2.nextElement());
            this.reqTime = ASN1GeneralizedTime.getInstance(var2.nextElement());
        }else {
            throw new IllegalArgumentException("sequence wrong size for a RequestData");
        }
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(this.version);
        vector.add(this.operand);
        vector.add(this.reqTime);
        return new DERSequence(vector);
    }

    public static RequestData getInstance(Object var0) {
        if (var0 instanceof RequestData) {
            return (RequestData)var0;
        } else {
            return var0 != null ? new RequestData(ASN1Sequence.getInstance(var0)) : null;
        }
    }

    @Override
    public String toString() {
        try {
            return String.format("RequestData: version=%d, operand=%d, reqtime=%s",
                    this.version.getValue().intValue(),
                    this.operand.getValue().intValue(),
                    this.reqTime.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
