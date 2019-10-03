package com.cy.transferapi.model;


import com.cy.transferapi.exception.EntityCreationException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private long version;

    @Column(unique = true, nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0L, message = "Account balance cannot be negative")
    private BigDecimal balance;

    protected Account() {
        super();
    }

    public Account(String name, BigDecimal initialBalance) throws EntityCreationException {

        this();

        this.validateBeforeCreation(name, initialBalance);

        this.name = name;
        this.balance = initialBalance;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public boolean hasEnoughBalanceForTransfer(BigDecimal transferAmount) {
        return this.balance.subtract(transferAmount).compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean subtract(BigDecimal transferAmount) {
        if (this.hasEnoughBalanceForTransfer(transferAmount)) {
            this.balance = this.balance.subtract(transferAmount);
            return true;
        }

        return false;
    }

    public void add(BigDecimal transferAmount) {
        this.balance = this.balance.add(transferAmount);
    }

    private void validateBeforeCreation(String name, BigDecimal initialBalance) throws EntityCreationException {
        try {
            name = Optional.of(name).get().trim();
            if (name.length() < 1) {
                throw new EntityCreationException("Account name must be provided");
            }

            initialBalance = Optional.of(initialBalance).get();
            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new EntityCreationException("Account initial balance cannot be negative");
            }
        } catch (NullPointerException e) {
            throw new EntityCreationException(e);
        }
    }
}
