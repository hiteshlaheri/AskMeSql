interface SqlResponse {
    response?: Array<Record<string, unknown> | undefined>;
    noofrows: number;
}
export default SqlResponse;
