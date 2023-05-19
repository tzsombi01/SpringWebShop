export interface CreditCardResponse {
    id: number,
    cardNumber: number,
    expiryDate?: Array<Date>,
    fullName: string,
    type: string,
    isActive: boolean
}