export class VersionAwareService {
    public getVersion() {
        const version = localStorage.getItem("apiVersion");
        if (!version) {
            return "v1";
        }
        return version;
    }

    public versionedUrl(baseUrl: string, resource: string) {
        return `${baseUrl}/${this.getVersion()}/${resource}`
    }
}