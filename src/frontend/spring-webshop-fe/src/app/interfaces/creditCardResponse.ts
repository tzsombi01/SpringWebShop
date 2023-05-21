export interface CreditCardResponse {
    id: number,
    cardNumber: number,
    expiryDate: Date,
    fullName: string,
    type: string,
    isActive: boolean
}