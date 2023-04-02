export interface Product {
    id: number,
    name: string,
    price: number,
    description?: string,
    sellerId: number,
    buyers?: string[],
    ramInGb?: number,
    manufacturer?: string,
    color?: string,
    system?: string,
    gpu?: string,
    cpu?: string
}