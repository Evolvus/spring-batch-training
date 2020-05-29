package com.evolvus.spring.batch;

import lombok.Data;

@Data
public class FileData {
	private String StsId;
	private String OrgnlInstrId;
    private String OrgnlEndToEndId;
    private String OrgnlTxId;
    private String TxSts;
    private String InstdAgt;
    private String OrgnlTxRef;    
	private StatusData StsRsnInf;
}
