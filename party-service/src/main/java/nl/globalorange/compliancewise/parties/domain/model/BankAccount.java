package nl.globalorange.compliancewise.parties.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.core.domain.BaseEntity;
import nl.globalorange.compliancewise.parties.domain.model.party.Party;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Currency;

import static java.lang.Boolean.FALSE;
import static javax.persistence.EnumType.STRING;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "bank_account")
@SequenceGenerator(name = "pk_sequence", sequenceName = "bank_account_id_seq", allocationSize = 1)
public class BankAccount extends BaseEntity {
    public static final String PATH = "bank-accounts";
    public static final String SEQUENCE_NAME = "bank_account_id_seq";

    @NotEmpty
    @Column(name = "holder_name")
    private String bankAccountHolderName;

    private String bic;
    private String iban;
    private String bban;
    private Currency currency;
    private String bank;

    @ManyToOne
    @JoinColumn(name = "party_id")
    @JsonIgnore
    private Party party;

    @Enumerated(STRING)
    @Column(name = "country_code")
    private CountryCode country;

    private String tag25Contents;

    @Column(name = "is_operational")
    private Boolean operational = FALSE;
    private Boolean deleted = FALSE;

    //Mandate bank
    private String mandateBankId;
    private String mandateBankName;
    private String mandateBankReference;

    //Clearing bank
    private String bankClearingCode;
    private String bankClearingCodeType;

    //Intermediate bank
    private String intermediateBankName;
    private String intermediateBankBic;
    private String intermediateBankCity;
    private String intermediateBankClearingCode;
    private String intermediateBankClearingCodeType;

    @Enumerated(STRING)
    @Column(name = "intermediate_bank_country_code")
    private CountryCode intermediateBankCountry;

}